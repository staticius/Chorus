package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType
import cn.nukkit.item.Item

class BlockOakLeaves(blockstate: BlockState?) : BlockLeaves(blockstate) {
    override val type: WoodType
        get() = WoodType.OAK

    override fun toSapling(): Item {
        return Item.get(BlockID.OAK_SAPLING)
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.OAK_LEAVES, CommonBlockProperties.PERSISTENT_BIT, CommonBlockProperties.UPDATE_BIT)
            get() = Companion.field
    }
}