package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.event.inventory.ItemStackRequestActionEvent
import org.chorus_oss.chorus.event.player.PlayerTransferItemEvent
import org.chorus_oss.chorus.inventory.Inventory
import org.chorus_oss.chorus.inventory.request.*
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.ItemStackRequestPacket
import org.chorus_oss.chorus.network.protocol.ItemStackResponsePacket
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.protocol.types.itemstack.ContainerSlotType
import org.chorus_oss.chorus.network.protocol.types.itemstack.request.ItemStackRequestSlotData
import org.chorus_oss.chorus.network.protocol.types.itemstack.request.action.*
import org.chorus_oss.chorus.network.protocol.types.itemstack.response.ItemStackResponse
import org.chorus_oss.chorus.network.protocol.types.itemstack.response.ItemStackResponseContainer
import org.chorus_oss.chorus.network.protocol.types.itemstack.response.ItemStackResponseSlot
import org.chorus_oss.chorus.network.protocol.types.itemstack.response.ItemStackResponseStatus
import org.chorus_oss.chorus.utils.Loggable
import java.util.*


class ItemStackRequestPacketProcessor : DataPacketProcessor<ItemStackRequestPacket>() {
    override fun handle(player: Player, pk: ItemStackRequestPacket) {
        val player = player.player
        val responses: MutableList<ItemStackResponse> = ArrayList()
        for (request in pk.requests) {
            val actions = request.actions
            val context = ItemStackRequestContext(request)
            val itemStackResponse = ItemStackResponse(ItemStackResponseStatus.OK, request.requestId, ArrayList())
            val responseContainerMap: MutableMap<ContainerSlotType, ItemStackResponseContainer> = LinkedHashMap()
            for (index in actions.indices) {
                val action = actions[index]
                context.currentActionIndex = (index)
                val processor = PROCESSORS[action.type] as ItemStackRequestActionProcessor<ItemStackRequestAction>?
                if (processor == null) {
                    log.warn("Unhandled inventory action type {}", action.type)
                    continue
                }

                val event = ItemStackRequestActionEvent(player, action, context)
                TransferItemEventCaller.call(event)
                Server.instance.pluginManager.callEvent(event)

                val response = if (event.response != null) {
                    event.response
                } else if (event.cancelled) {
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
            itemStackResponse.containers.addAll(responseContainerMap.values)
            responses.add(itemStackResponse)
        }

        for (r in responses) {
            for (c in r.containers) {
                val newItems = LinkedHashMap<Int, ItemStackResponseSlot>()
                for (i in c.items) {
                    newItems[Objects.hash(i.slot, i.hotbarSlot)] = i
                }
                c.items.clear()
                c.items.addAll(newItems.values)
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
            val sourceInventory = NetworkMapping.getInventory(player, transferResult.source.containerName.container)
            val sourceSlot = sourceInventory.fromNetworkSlot(transferResult.source.slot)

            val destinationInventory = transferResult.destination
                .map { destination ->
                    NetworkMapping.getInventory(
                        player,
                        destination.containerName.container
                    )
                }
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
            if (transferEvent.cancelled) {
                event.cancelled = true
            }
        }


        fun handleAction(action: ItemStackRequestAction): TransferResult? {
            return when (action) {
                is TransferItemStackRequestAction -> TransferResult(
                    action.source,
                    Optional.of<ItemStackRequestSlotData>(action.destination),
                    PlayerTransferItemEvent.Type.TRANSFER
                )

                is SwapAction -> TransferResult(
                    action.source,
                    Optional.of<ItemStackRequestSlotData>(action.destination),
                    PlayerTransferItemEvent.Type.SWAP
                )

                is DropAction -> TransferResult(
                    action.source,
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

    companion object : Loggable {
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
