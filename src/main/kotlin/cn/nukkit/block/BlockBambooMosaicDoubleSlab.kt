package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

class BlockBambooMosaicDoubleSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockDoubleSlabBase(blockstate) {
    override fun getSlabName(): String {
        return "Bamboo Mosaic"
    }

    override fun getSingleSlab(): BlockState {
        return BlockBambooMosaicSlab.Companion.PROPERTIES.getDefaultState()
    }

    override val burnChance: Int
        get() = 5

    override val burnAbility: Int
        get() = 20

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    companion object {
        val properties: BlockProperties =
            BlockProperties(BAMBOO_MOSAIC_DOUBLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}