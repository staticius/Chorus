package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType
import cn.nukkit.item.*

class BlockDarkOakLeaves(blockstate: BlockState?) : BlockLeaves(blockstate) {
    override fun getType(): WoodType {
        return WoodType.DARK_OAK
    }

    override fun toSapling(): Item {
        return Item.get(DARK_OAK_SAPLING)
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(DARK_OAK_LEAVES, CommonBlockProperties.PERSISTENT_BIT, CommonBlockProperties.UPDATE_BIT)
            get() = Companion.field
    }
}