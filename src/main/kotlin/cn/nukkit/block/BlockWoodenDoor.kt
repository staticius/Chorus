package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

open class BlockWoodenDoor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockDoor(blockstate) {
    override val name: String
        get() = "Wood Door Block"

    override val hardness: Double
        get() = 3.0

    override val resistance: Double
        get() = 15.0

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.WOODEN_DOOR,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPPER_BLOCK_BIT,
            CommonBlockProperties.DOOR_HINGE_BIT
        )
            get() = Companion.field
    }
}