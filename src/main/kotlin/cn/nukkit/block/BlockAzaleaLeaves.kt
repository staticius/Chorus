package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType
import cn.nukkit.item.*

open class BlockAzaleaLeaves @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockLeaves(blockState) {
    override val name: String
        get() = "Azalea Leaves"

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override fun canHarvest(item: Item): Boolean {
        return item.isShears
    }

    /*这里写木质类型为OAK只是为了获取凋落物时的概率正确，并不代表真的就是橡木*/
    override fun getType(): WoodType {
        return WoodType.OAK
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(AZALEA_LEAVES, CommonBlockProperties.PERSISTENT_BIT, CommonBlockProperties.UPDATE_BIT)
            get() = Companion.field
    }
}
