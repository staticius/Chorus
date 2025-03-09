package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.level.Sound

class BlockBambooFenceGate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFenceGate(blockstate) {
    override val name: String
        get() = "Bamboo Fence Gate"

    override fun playOpenSound() {
        level.addSound(this.position, Sound.OPEN_BAMBOO_WOOD_FENCE_GATE)
    }

    override fun playCloseSound() {
        level.addSound(this.position, Sound.CLOSE_BAMBOO_WOOD_FENCE_GATE)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            BAMBOO_FENCE_GATE,
            CommonBlockProperties.IN_WALL_BIT,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.OPEN_BIT
        )
            get() = Companion.field
    }
}