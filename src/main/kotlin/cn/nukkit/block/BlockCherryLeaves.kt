package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType
import cn.nukkit.item.*

class BlockCherryLeaves @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockLeaves(blockState) {
    override val name: String
        get() = "Cherry Leaves"

    /*这里写木质类型为BIRCH只是为了获取凋落物时的概率正确，并不代表真的就是白桦木*/
    override fun getType(): WoodType {
        return WoodType.CHERRY
    }

    override fun toSapling(): Item {
        return Item.get(CHERRY_SAPLING)
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(CHERRY_LEAVES, CommonBlockProperties.PERSISTENT_BIT, CommonBlockProperties.UPDATE_BIT)
            get() = Companion.field
    }
}
