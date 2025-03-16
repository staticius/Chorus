package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.item.ItemBirchSign

class BlockBirchWallSign : BlockWallSign {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState) : super(blockState)

    override val name: String
        get() = "Birch Wall Sign"

    override fun getWallSignId(): String {
        return BlockID.BIRCH_WALL_SIGN
    }

    override fun getStandingSignId(): String {
        return BlockID.BIRCH_WALL_SIGN
    }

    override fun toItem(): Item {
        return ItemBirchSign()
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.BIRCH_WALL_SIGN, CommonBlockProperties.FACING_DIRECTION)
    }
}
