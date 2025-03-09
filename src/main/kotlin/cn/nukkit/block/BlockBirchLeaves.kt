package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType
import cn.nukkit.item.*

class BlockBirchLeaves(blockstate: BlockState?) : BlockLeaves(blockstate) {
    override fun getType(): WoodType {
        return WoodType.BIRCH
    }

    override fun toSapling(): Item {
        return Item.get(BIRCH_SAPLING)
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BIRCH_LEAVES, CommonBlockProperties.PERSISTENT_BIT, CommonBlockProperties.UPDATE_BIT)
            get() = Companion.field
    }
}