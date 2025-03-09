package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockStrippedWarpedStem @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockStemStripped(blockstate) {
    override val name: String
        get() = "Stripped Warped Stem"

    override val burnChance: Int
        get() = 0

    override val burnAbility: Int
        get() = 0

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STRIPPED_WARPED_STEM, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}