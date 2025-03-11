package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.*

class BlockBambooWallSign @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockWallSign(blockState) {
    override val name: String
        get() = "Bamboo Wall Sign"

    override fun getStandingSignId(): String {
        return BAMBOO_STANDING_SIGN
    }

    override fun getWallSignId(): String {
        return BlockAcaciaWallSign.properties.identifier
    }

    override fun toItem(): Item? {
        return ItemBambooSign()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.BAMBOO_WALL_SIGN, CommonBlockProperties.FACING_DIRECTION)

    }
}