package org.chorus.inventory.fake

import org.chorus.Player
import org.chorus.entity.EntityHumanType.getInventory
import org.chorus.event.inventory.ItemStackRequestActionEvent
import org.chorus.inventory.*
import org.chorus.item.*
import org.chorus.network.protocol.ContainerClosePacket
import org.chorus.network.protocol.ContainerOpenPacket
import org.chorus.network.protocol.types.itemstack.ContainerSlotType
import org.chorus.network.protocol.types.itemstack.request.ItemStackRequestSlotData
import org.chorus.network.protocol.types.itemstack.request.action.DropAction
import org.chorus.network.protocol.types.itemstack.request.action.SwapAction
import org.chorus.network.protocol.types.itemstack.request.action.TransferItemStackRequestAction
import org.chorus.plugin.InternalPlugin
import org.chorus.recipe.*
import org.jetbrains.annotations.ApiStatus
import java.util.List

class FakeInventory @JvmOverloads constructor(
    @JvmField val fakeInventoryType: FakeInventoryType,
    var title: String = fakeInventoryType.name
) :
    BaseInventory(null, fakeInventoryType.inventoryType, fakeInventoryType.size), InputInventory {
    private val handlers: MutableMap<Int, ItemHandler> = HashMap()
    private val fakeBlock: FakeBlock = fakeInventoryType.fakeBlock
    private var defaultItemHandler: ItemHandler

    init {
        this.defaultItemHandler =
            ItemHandler { a: FakeInventory?, b: Int, c: Item?, d: Item?, e: ItemStackRequestActionEvent? -> }

        when (fakeInventoryType) {
            FakeInventoryType.CHEST, FakeInventoryType.DOUBLE_CHEST, FakeInventoryType.HOPPER, FakeInventoryType.DISPENSER, FakeInventoryType.DROPPER, FakeInventoryType.ENDER_CHEST -> {
                val map = super.slotTypeMap()
                for (i in 0..<getSize()) {
                    map[i] = ContainerSlotType.LEVEL_ENTITY
                }
            }

            FakeInventoryType.FURNACE -> {
                val map = super.slotTypeMap()
                map[0] = ContainerSlotType.FURNACE_INGREDIENT
                map[1] = ContainerSlotType.FURNACE_FUEL
                map[2] = ContainerSlotType.FURNACE_RESULT
            }

            FakeInventoryType.BREWING_STAND -> {
                val map = super.slotTypeMap()
                map[0] = ContainerSlotType.BREWING_INPUT
                map[1] = ContainerSlotType.BREWING_RESULT
                map[2] = ContainerSlotType.BREWING_RESULT
                map[3] = ContainerSlotType.BREWING_RESULT
                map[4] = ContainerSlotType.BREWING_FUEL
            }

            FakeInventoryType.SHULKER_BOX -> {
                val map = super.slotTypeMap()
                for (i in 0..<getSize()) {
                    map[i] = ContainerSlotType.SHULKER_BOX
                }
            }

            FakeInventoryType.WORKBENCH -> {
                val map = super.networkSlotMap()
                for (i in 0..<getSize()) {
                    map[i] = 32 + i
                }
                val map2 = super.slotTypeMap()
                for (i in 0..<getSize()) {
                    map2[i] = ContainerSlotType.CRAFTING_INPUT
                }
            }
        }
    }

    override var holder: InventoryHolder?
        get() = null
        set(holder) {
            super.holder = holder
        }

    override fun onOpen(player: Player) {
        player.fakeInventoryOpen = true
        fakeBlock.create(player, this.title)
        player.level!!.scheduler.scheduleDelayedTask(InternalPlugin.INSTANCE, {
            val first = fakeBlock.getLastPositions(player).stream().findFirst()
            if (first.isPresent) {
                val position = first.get()
                player.dataPacket(ContainerOpenPacket(
                    containerID = player.getWindowId(this),
                    containerType = type.networkType,
                    position = position.vector3.asBlockVector3(),
                    targetActorID = player.getId()
                ))
                super.onOpen(player)
                this.sendContents(player)
            } else {
                fakeBlock.remove(player)
            }
        }, 5)
    }

    override fun onClose(player: Player) {
        val containerId = player.getWindowId(this)
        player.dataPacket(ContainerClosePacket(
            containerID = containerId,
            containerType = type,
            serverInitiatedClose = player.closingWindowId != containerId
        ))
        player.level!!.scheduler.scheduleDelayedTask(
            InternalPlugin.INSTANCE,
            {
                fakeBlock.remove(player)
            }, 5
        )
        super.onClose(player)
        player.fakeInventoryOpen = false
    }

    fun setItemHandler(index: Int, handler: ItemHandler) {
        handlers[index] = handler
    }

    fun setDefaultItemHandler(defaultItemHandler: ItemHandler) {
        this.defaultItemHandler = defaultItemHandler
    }

    @ApiStatus.Internal
    fun handle(event: ItemStackRequestActionEvent) {
        val action = event.action
        var source: ItemStackRequestSlotData? = null
        var destination: ItemStackRequestSlotData? = null
        if (action is TransferItemStackRequestAction) {
            source = action.source
            destination = action.destination
        } else if (action is SwapAction) {
            source = action.source
            destination = action.destination
        } else if (action is DropAction) {
            source = action.source
        }
        if (source != null) {
            val sourceSlotType = source.containerName.container
            val sourceI: Inventory = getInventory(event.player, sourceSlotType)
            val sourceSlot = sourceI.fromNetworkSlot(source.slot)
            val sourItem = sourceI.getItem(sourceSlot)
            if (sourceI == this) {
                val handler = handlers.getOrDefault(sourceSlot, this.defaultItemHandler)
                handler.handle(this, sourceSlot, sourItem, Item.AIR, event)
            } else if (destination != null) {
                val destinationSlotType = destination.containerName.container
                val destinationI: Inventory = getInventory(event.player, destinationSlotType)
                val destinationSlot = destinationI.fromNetworkSlot(destination.slot)
                val destItem = destinationI.getItem(destinationSlot)
                if (destinationI == this) {
                    val handler = handlers.getOrDefault(destinationSlot, this.defaultItemHandler)
                    handler.handle(this, destinationSlot, destItem, sourItem, event)
                }
            }
        }
    }

    override val input: Input
        get() {
            if (fakeInventoryType == FakeInventoryType.WORKBENCH) {
                val item1 =
                    List.of(getItem(0), getItem(1), getItem(2))
                        .toArray<Item>(Item.EMPTY_ARRAY)
                val item2 =
                    List.of(getItem(3), getItem(4), getItem(5))
                        .toArray<Item>(Item.EMPTY_ARRAY)
                val item3 =
                    List.of(getItem(6), getItem(7), getItem(8))
                        .toArray<Item>(Item.EMPTY_ARRAY)
                val items =
                    arrayOf(item1, item2, item3)
                return Input(3, 3, items)
            } else {
                return Input(
                    3,
                    3,
                    Array(3) { arrayOfNulls(3) })
            }
        }
}
