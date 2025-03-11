package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.*
import org.chorus.math.BlockFace

/**
 * @author CreeperFace
 * @since 2015/11/22
 */
class BlockDaylightDetectorInverted @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockDaylightDetector(blockState) {
    override val name: String
        get() = "Daylight Detector Inverted"

    override fun toItem(): Item? {
        return ItemBlock(get(DAYLIGHT_DETECTOR), 0)
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (isNotActivate(player)) return false
        val block = BlockDaylightDetector()
        level.setBlock(this.position, block, true, true)
        if (Server.instance.settings.levelSettings().enableRedstone()) {
            block.updatePower()
        }
        return true
    }

    override val isInverted: Boolean
        get() = true

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.DAYLIGHT_DETECTOR_INVERTED, CommonBlockProperties.REDSTONE_SIGNAL)

    }
}
