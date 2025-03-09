package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.Item
import cn.nukkit.math.BlockFace

/**
 * Alias still water
 */
class BlockWater @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockFlowingWater(blockstate) {
    override fun place(
        item: Item,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player?
    ): Boolean {
        return level.setBlock(this.position, this, true, false)
    }

    override val name: String
        get() = "Still Water"

    override fun getLiquidWithNewDepth(depth: Int): BlockLiquid {
        return BlockWater(
            blockState!!.setPropertyValue(
                Companion.properties,
                CommonBlockProperties.LIQUID_DEPTH.createValue(depth)
            )
        )
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WATER, CommonBlockProperties.LIQUID_DEPTH)
            get() = Companion.field
    }
}
