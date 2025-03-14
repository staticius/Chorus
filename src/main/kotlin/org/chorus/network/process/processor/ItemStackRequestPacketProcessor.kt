package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.Server
import org.chorus.dialog.handler.FormDialogHandler.handle
import org.chorus.entity.EntityHumanType.getInventory
import org.chorus.entity.item.EntityChestBoat.getInventory
import org.chorus.entity.mob.animal.EntityHorse.getInventory
import org.chorus.event.inventory.ItemStackRequestActionEvent
import org.chorus.event.player.PlayerTransferItemEvent
import org.chorus.inventory.Inventory
import org.chorus.inventory.fake.FakeInventory
import org.chorus.inventory.fake.FakeInventory.handle
import org.chorus.inventory.request.*
import org.chorus.inventory.request.ItemStackRequestActionProcessor.handle
import org.chorus.inventory.request.NetworkMapping.getInventory
import org.chorus.network.connection.BedrockSession.handle
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.ItemStackRequestPacket
import org.chorus.network.protocol.ItemStackResponsePacket
import org.chorus.network.protocol.ProtocolInfo
import org.chorus.network.protocol.types.itemstack.ContainerSlotType
import org.chorus.network.protocol.types.itemstack.request.ItemStackRequestSlotData
import org.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestAction
import org.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import org.chorus.network.protocol.types.itemstack.response.ItemStackResponse
import org.chorus.network.protocol.types.itemstack.response.ItemStackResponseContainer
import org.chorus.network.protocol.types.itemstack.response.ItemStackResponseSlot
import org.chorus.network.protocol.types.itemstack.response.ItemStackResponseStatus
import java.util.*
import java.util.function.Function
import kotlin.collections.set


class ItemStackRequestPacketProcessor : DataPacketProcessor<ItemStackRequestPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: ItemStackRequestPacket) {
        val player = playerHandle.player
        val responses: MutableList<ItemStackResponse> = ArrayList()
        for (request in pk.requests) {
            val actions = request.actions
            val context = ItemStackRequestContext(request)
            val itemStackResponse = ItemStackResponse(ItemStackResponseStatus.OK, request.requestId, ArrayList())
            val responseContainerMap: MutableMap<ContainerSlotType, ItemStackResponseContainer> = LinkedHashMap()
            for (index in actions.indices) {
                val action = actions[index]
                context.setCurrentActionIndex(index)
                val processor = PROCESSORS[action.type] as ItemStackRequestActionProcessor<ItemStackRequestAction>?
                if (processor == null) {
                    ItemStackRequestPacketProcessor.log.warn("Unhandled inventory action type {}", action.type)
                    continue
                }

                val event = ItemStackRequestActionEvent(player, action, context)
                TransferItemEventCaller.call(event)
                Server.instance.pluginManager.callEvent(event)
                val topWindow = player.topWindow
                if (topWindow.isPresent && topWindow.get() is FakeInventory) {
                    fakeInventory.handle(event)
                }
                val response = if (event.response != null) {
                    event.response
                } else if (event.isCancelled) {
                    context.error()
                } else {
                    processor.handle(action, player, context)
                }
                if (response != null) {
                    if (!response.ok) {
                        itemStackResponse.result = ItemStackResponseStatus.ERROR
                        itemStackResponse.containers.clear()
                        responses.add(itemStackResponse)
                        break
                    }

                    for (container in response.containers) {
                        responseContainerMap.compute(
                            container.containerName.container
                        ) { key: ContainerSlotType?, oldValue: ItemStackResponseContainer? ->
                            if (oldValue == null) {
                                return@compute container
                            } else {
                                oldValue.items.addAll(container.items)
                                return@compute oldValue
                            }
                        }
                    }
                }
            }
            itemStackResponse.containers.addAll(responseContainerMap.values())
            responses.add(itemStackResponse)
        }

        for (r in responses) {
            for (c in r.containers) {
                val newItems = LinkedHashMap<Int, ItemStackResponseSlot>()
                for (i in c.items) {
                    newItems[Objects.hash(i.slot, i.hotbarSlot)] = i
                }
                c.items.clear()
                c.items.addAll(newItems.values())
            }
        }

        val itemStackResponsePacket = ItemStackResponsePacket()
        itemStackResponsePacket.entries.addAll(responses)
        player.dataPacket(itemStackResponsePacket)
    }

    override val packetId: Int
        get() = ProtocolInfo.ITEM_STACK_REQUEST_PACKET


    private object TransferItemEventCaller {
        fun call(event: ItemStackRequestActionEvent) {
            val action = event.action

            val transferResult = handleAction(action) ?: return

            val player = event.player
            val sourceInventory: Inventory =
                NetworkMapping.getInventory(player, transferResult.source.containerName.container)
            val sourceSlot = sourceInventory.fromNetworkSlot(transferResult.source.slot)

            val destinationInventory = transferResult.destination
                .map<Inventory?>(Function<ItemStackRequestSlotData, Inventory?> { destination: ItemStackRequestSlotData ->
                    NetworkMapping.getInventory(
                        player,
                        destination.containerName.container
                    )
                })
            val destinationSlot = destinationInventory
                .flatMap { inventory: Inventory? ->
                    transferResult.destination
                        .map { destination: ItemStackRequestSlotData -> inventory!!.fromNetworkSlot(destination.slot) }
                }
            val destinationItem = destinationSlot
                .flatMap { slot: Int? ->
                    destinationInventory.flatMap { inventory: Inventory? ->
                        Optional.of(
                            inventory!!.getItem(slot!!)
                        )
                    }
                }

            val transferEvent = PlayerTransferItemEvent(
                player,
                transferResult.type,
                sourceInventory.getItem(sourceSlot),
                destinationItem.orElse(null),
                sourceSlot,
                destinationSlot.orElse(-1),
                sourceInventory,
                destinationInventory.orElse(null)
            )

            Server.instance.pluginManager.callEvent(transferEvent)
            if (transferEvent.isCancelled) {
                event.isCancelled = true
            }
        }


        fun handleAction(action: ItemStackRequestAction): TransferResult? {
            return when (action) {
                -> TransferResult(
                    transfer.getSource(),
                    Optional.of<ItemStackRequestSlotData>(transfer.getDestination()),
                    PlayerTransferItemEvent.Type.TRANSFER
                )

                -> TransferResult(
                    swap.getSource(),
                    Optional.of<ItemStackRequestSlotData>(swap.getDestination()),
                    PlayerTransferItemEvent.Type.SWAP
                )

                -> TransferResult(
                    drop.getSource(),
                    Optional.empty<ItemStackRequestSlotData>(),
                    PlayerTransferItemEvent.Type.DROP
                )

                else -> null
            }
        }


        @JvmRecord
        private data class TransferResult(
            val source: ItemStackRequestSlotData,
            val destination: Optional<ItemStackRequestSlotData>,
            val type: PlayerTransferItemEvent.Type
        )
    }

    companion object {
        val PROCESSORS: EnumMap<ItemStackRequestActionType, ItemStackRequestActionProcessor<*>> = EnumMap(
            ItemStackRequestActionType::class.java
        )

        init {
            PROCESSORS[ItemStackRequestActionType.CONSUME] =
                ConsumeActionProcessor()
            PROCESSORS[ItemStackRequestActionType.CRAFT_CREATIVE] =
                CraftCreativeActionProcessor()
            PROCESSORS[ItemStackRequestActionType.CRAFT_RECIPE] =
                CraftRecipeActionProcessor()
            PROCESSORS[ItemStackRequestActionType.CRAFT_RESULTS_DEPRECATED] =
                CraftResultDeprecatedActionProcessor()
            PROCESSORS[ItemStackRequestActionType.CRAFT_RECIPE_AUTO] =
                CraftRecipeAutoProcessor()
            PROCESSORS[ItemStackRequestActionType.CREATE] =
                CreateActionProcessor()
            PROCESSORS[ItemStackRequestActionType.DESTROY] =
                DestroyActionProcessor()
            PROCESSORS[ItemStackRequestActionType.DROP] =
                DropActionProcessor()
            PROCESSORS[ItemStackRequestActionType.PLACE] =
                PlaceActionProcessor()
            PROCESSORS[ItemStackRequestActionType.SWAP] =
                SwapActionProcessor()
            PROCESSORS[ItemStackRequestActionType.TAKE] =
                TakeActionProcessor()
            PROCESSORS[ItemStackRequestActionType.CRAFT_RECIPE_OPTIONAL] =
                CraftRecipeOptionalProcessor()
            PROCESSORS[ItemStackRequestActionType.CRAFT_REPAIR_AND_DISENCHANT] =
                CraftGrindstoneActionProcessor()
            PROCESSORS[ItemStackRequestActionType.MINE_BLOCK] =
                MineBlockActionProcessor()
            PROCESSORS[ItemStackRequestActionType.CRAFT_LOOM] =
                CraftLoomActionProcessor()
            PROCESSORS[ItemStackRequestActionType.BEACON_PAYMENT] =
                BeaconPaymentActionProcessor()
        }
    }
}
