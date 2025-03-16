package org.chorus.block

import org.chorus.block.property.CommonBlockProperties


class BlockPolishedBasalt @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockBasalt(blockstate) {
    override val name: String
        get() = "Polished Basalt"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.POLISHED_BASALT, CommonBlockProperties.PILLAR_AXIS)

    }
}
