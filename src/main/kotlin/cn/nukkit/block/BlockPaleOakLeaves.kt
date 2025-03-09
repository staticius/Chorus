package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType
import cn.nukkit.item.*

class BlockPaleOakLeaves(blockstate: BlockState?) : BlockLeaves(blockstate) {
    override val type: WoodType
        get() = WoodType.PALE_OAK

    override fun toSapling(): Item {
        return Item.get(BlockID.PALE_OAK_SAPLING)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.PALE_OAK_LEAVES,
            CommonBlockProperties.PERSISTENT_BIT,
            CommonBlockProperties.UPDATE_BIT
        )
            get() = Companion.field
    }
}