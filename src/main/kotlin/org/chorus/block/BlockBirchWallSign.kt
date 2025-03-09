package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.*

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
        val properties: BlockProperties = BlockProperties(BIRCH_WALL_SIGN, CommonBlockProperties.FACING_DIRECTION)
            get() = Companion.field
    }
}
