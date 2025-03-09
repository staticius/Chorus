package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.*

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
        val properties: BlockProperties = BlockProperties(BAMBOO_WALL_SIGN, CommonBlockProperties.FACING_DIRECTION)
            get() = Companion.field
    }
}