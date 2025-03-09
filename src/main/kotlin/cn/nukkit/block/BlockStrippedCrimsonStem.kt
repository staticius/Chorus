package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockStrippedCrimsonStem @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockStemStripped(blockstate) {
    override val name: String
        get() = "Stripped Crimson Stem"

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STRIPPED_CRIMSON_STEM, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}