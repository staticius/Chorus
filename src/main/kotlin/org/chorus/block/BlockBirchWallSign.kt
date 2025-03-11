package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.*

class BlockBirchWallSign : BlockWallSign {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    override val name: String
        get() = "Birch Wall Sign"

    override fun getWallSignId(): String {
        return BIRCH_WALL_SIGN
    }

    override fun getStandingSignId(): String {
        return BIRCH_WALL_SIGN
    }

    override fun toItem(): Item? {
        return ItemBirchSign()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BIRCH_WALL_SIGN, CommonBlockProperties.FACING_DIRECTION)

    }
}
