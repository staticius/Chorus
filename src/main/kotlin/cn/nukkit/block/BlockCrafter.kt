package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockCrafter @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.CRAFTER,
            CommonBlockProperties.CRAFTING,
            CommonBlockProperties.ORIENTATION,
            CommonBlockProperties.TRIGGERED_BIT
        )
            get() = Companion.field
    }
}