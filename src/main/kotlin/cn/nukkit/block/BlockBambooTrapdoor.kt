package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.level.Sound

class BlockBambooTrapdoor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockTrapdoor(blockstate) {
    override val name: String
        get() = "Bamboo Trapdoor"

    override fun playOpenSound() {
        level.addSound(this.position, Sound.OPEN_BAMBOO_WOOD_TRAPDOOR)
    }

    override fun playCloseSound() {
        level.addSound(this.position, Sound.CLOSE_BAMBOO_WOOD_TRAPDOOR)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            BAMBOO_TRAPDOOR,
            CommonBlockProperties.DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPSIDE_DOWN_BIT
        )
            get() = Companion.field
    }
}