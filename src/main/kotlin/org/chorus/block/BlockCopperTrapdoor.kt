package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

open class BlockCopperTrapdoor @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockTrapdoor(blockstate) {

    override val name: String
        get() = "Copper Trapdoor"

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.COPPER_TRAPDOOR,
            CommonBlockProperties.DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPSIDE_DOWN_BIT
        )
    }
}