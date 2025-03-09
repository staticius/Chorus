package org.chorus.inventory.fake

import org.chorus.block.BlockID
import org.chorus.blockentity.*
import org.chorus.inventory.InventoryType

enum class FakeInventoryType(val inventoryType: InventoryType, val fakeBlock: FakeBlock, val size: Int) {
    CHEST(InventoryType.CONTAINER, SingleFakeBlock(BlockID.CHEST, BlockEntity.CHEST), 27),
    DOUBLE_CHEST(InventoryType.CONTAINER, DoubleFakeBlock(BlockID.CHEST, BlockEntity.CHEST), 27 * 2),
    ENDER_CHEST(InventoryType.CONTAINER, SingleFakeBlock(BlockID.ENDER_CHEST, BlockEntity.ENDER_CHEST), 27),
    FURNACE(InventoryType.FURNACE, SingleFakeBlock(BlockID.FURNACE, BlockEntity.FURNACE), 3),
    BREWING_STAND(InventoryType.BREWING_STAND, SingleFakeBlock(BlockID.BREWING_STAND, BlockEntity.BREWING_STAND), 5),
    DISPENSER(InventoryType.DISPENSER, SingleFakeBlock(BlockID.DISPENSER, BlockEntity.DISPENSER), 9),
    DROPPER(InventoryType.DROPPER, SingleFakeBlock(BlockID.DROPPER, BlockEntity.DROPPER), 9),
    HOPPER(InventoryType.HOPPER, SingleFakeBlock(BlockID.HOPPER, BlockEntity.HOPPER), 5),
    SHULKER_BOX(InventoryType.CONTAINER, SingleFakeBlock(BlockID.UNDYED_SHULKER_BOX, BlockEntity.SHULKER_BOX), 27),
    WORKBENCH(InventoryType.WORKBENCH, SingleFakeBlock(BlockID.CRAFTING_TABLE, "default"), 9);

    val isCraftType: Boolean
        get() = this == WORKBENCH || this == FURNACE || this == BREWING_STAND
}
