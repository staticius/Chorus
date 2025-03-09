package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockAcaciaHangingSign : BlockHangingSign {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override val name: String
        get() = "Acacia Hanging Sign"

    companion object {
        val properties: BlockProperties = BlockProperties(
            ACACIA_HANGING_SIGN,
            CommonBlockProperties.ATTACHED_BIT,
            CommonBlockProperties.FACING_DIRECTION,
            CommonBlockProperties.GROUND_SIGN_DIRECTION,
            CommonBlockProperties.HANGING
        )
            get() = Companion.field
    }
}