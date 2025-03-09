package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType
import cn.nukkit.item.*

class BlockJungleLeaves(blockstate: BlockState?) : BlockLeaves(blockstate) {
    override fun getType(): WoodType {
        return WoodType.JUNGLE
    }

    override fun toSapling(): Item {
        return Item.get(BlockID.Companion.JUNGLE_SAPLING)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.Companion.JUNGLE_LEAVES,
            CommonBlockProperties.PERSISTENT_BIT,
            CommonBlockProperties.UPDATE_BIT
        )
            get() = Companion.field
    }
}