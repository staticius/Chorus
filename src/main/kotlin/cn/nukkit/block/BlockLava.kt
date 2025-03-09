package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.*
import cn.nukkit.math.BlockFace

/**
 * Alias STILL LAVA
 *
 * @author Angelic47 (Nukkit Project)
 */
class BlockLava @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFlowingLava(blockstate) {
    override val name: String
        get() = "Still Lava"

    override fun getLiquidWithNewDepth(depth: Int): BlockLiquid {
        return BlockLava(
            blockState!!.setPropertyValue(
                Companion.properties,
                CommonBlockProperties.LIQUID_DEPTH.createValue(depth)
            )
        )
    }

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

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LAVA, CommonBlockProperties.LIQUID_DEPTH)
            get() = Companion.field
    }
}
