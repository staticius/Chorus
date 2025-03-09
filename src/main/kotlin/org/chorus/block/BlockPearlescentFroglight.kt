package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockPearlescentFroglight @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockFroglight(blockState) {
    override val name: String
        get() = "Pearlescent Froglight"

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.PEARLESCENT_FROGLIGHT, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}
