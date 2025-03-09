package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.level.Sound

class BlockCrimsonTrapdoor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockTrapdoor(blockstate) {
    override val name: String
        get() = "Crimson Trapdoor"

    override val burnChance: Int
        get() = 0

    override val burnAbility: Int
        get() = 0

    override fun playOpenSound() {
        level.addSound(this.position, Sound.OPEN_NETHER_WOOD_TRAPDOOR)
    }

    override fun playCloseSound() {
        level.addSound(this.position, Sound.CLOSE_NETHER_WOOD_TRAPDOOR)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            CRIMSON_TRAPDOOR,
            CommonBlockProperties.DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPSIDE_DOWN_BIT
        )
            get() = Companion.field
    }
}