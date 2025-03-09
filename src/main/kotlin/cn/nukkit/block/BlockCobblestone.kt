package cn.nukkit.block

import cn.nukkit.item.ItemTool

/**
 * @author Angelic47 (Nukkit Project)
 */
class BlockCobblestone @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockState) {
    override val hardness: Double
        get() = 2.0

    override val resistance: Double
        get() = 30.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val name: String
        get() = "Cobblestone"

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(COBBLESTONE)
            get() = Companion.field
    }
}
