package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.*

class BlockPurpurBlock @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    override val name: String
        get() = "Purpur"

    override val hardness: Double
        get() = 1.5

    override val resistance: Double
        get() = 30.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun toItem(): Item? {
        return ItemBlock(properties.defaultState.toBlock())
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PURPUR_BLOCK, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}