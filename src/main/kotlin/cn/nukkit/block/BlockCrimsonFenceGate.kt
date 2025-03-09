package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.level.Sound

class BlockCrimsonFenceGate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFenceGate(blockstate) {
    override val name: String
        get() = "Crimson Fence Gate"

    override val burnChance: Int
        get() = 0

    override val burnAbility: Int
        get() = 0

    override fun playOpenSound() {
        level.addSound(this.position, Sound.OPEN_NETHER_WOOD_FENCE_GATE)
    }

    override fun playCloseSound() {
        level.addSound(this.position, Sound.CLOSE_NETHER_WOOD_FENCE_GATE)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            CRIMSON_FENCE_GATE,
            CommonBlockProperties.IN_WALL_BIT,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.OPEN_BIT
        )
            get() = Companion.field
    }
}