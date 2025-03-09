package cn.nukkit.block

import cn.nukkit.item.ItemTool

class BlockWarpedWartBlock @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockSolid(blockstate) {
    override val name: String
        get() = "Warped Wart Block"

    override val toolType: Int
        // TODO Fix it in https://github.com/PowerNukkit/PowerNukkit/pull/370, the same for BlockNetherWartBlock
        get() = ItemTool.TYPE_HANDS_ONLY //TODO Correct type is hoe

    override val resistance: Double
        get() = 1.0

    override val hardness: Double
        get() = 1.0

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WARPED_WART_BLOCK)
            get() = Companion.field
    }
}
