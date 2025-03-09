package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockVerdantFroglight @JvmOverloads constructor(blockState: BlockState? = Companion.properties.getDefaultState()) :
    BlockFroglight(blockState) {
    override val name: String
        get() = "Verdant Froglight"

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.VERDANT_FROGLIGHT,
            CommonBlockProperties.PILLAR_AXIS
        )
            get() = Companion.field
    }
}
