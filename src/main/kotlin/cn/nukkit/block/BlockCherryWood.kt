package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType

class BlockCherryWood @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockWood(blockstate) {
    override val name: String
        get() = "Cherry Wood"

    override fun getWoodType(): WoodType {
        return WoodType.CHERRY
    }

    companion object {
        val properties: BlockProperties = BlockProperties(CHERRY_WOOD, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}