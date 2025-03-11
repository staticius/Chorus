package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.*
import org.chorus.level.Sound
import org.chorus.math.BlockFace

class BlockIronTrapdoor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockTrapdoor(blockstate) {
    override val name: String
        get() = "Iron Trapdoor"

    override val hardness: Double
        get() = 5.0

    override val resistance: Double
        get() = 25.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        return false
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun playOpenSound() {
        level.addSound(this.position, Sound.OPEN_IRON_TRAPDOOR)
    }

    override fun playCloseSound() {
        level.addSound(this.position, Sound.CLOSE_IRON_TRAPDOOR)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
BlockID.BlockID.Companion.IRON_TRAPDOOR,
            CommonBlockProperties.DIRECTION,
            CommonBlockProperties.OPEN_BIT,
            CommonBlockProperties.UPSIDE_DOWN_BIT
        )

    }
}