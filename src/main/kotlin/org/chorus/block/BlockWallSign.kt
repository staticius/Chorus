package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.IntPropertyType
import org.chorus.level.Level
import org.chorus.math.BlockFace
import org.chorus.math.BlockFace.Companion.fromIndex
import org.chorus.math.CompassRoseDirection

/**
 * @author Pub4Game
 * @since 26.12.2015
 */
open class BlockWallSign @JvmOverloads constructor(blockState: BlockState = Companion.properties.getDefaultState()) :
    BlockStandingSign(blockState) {
    override val wallSignId: String
        get() = id

    override val standingSignId: String?
        get() = BlockID.STANDING_SIGN

    override val name: String
        get() = "Wall Sign"

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (getSide(blockFace.getOpposite()).isAir) {
                level.useBreakOn(this.position)
            }
            return Level.BLOCK_UPDATE_NORMAL
        }
        return 0
    }

    override var blockFace: BlockFace
        get() = fromIndex(getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.FACING_DIRECTION))
        set(face) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.FACING_DIRECTION, face.index)
        }

    override var getSignDirection: CompassRoseDirection?
        get() = blockFace.compassRoseDirection
        set(direction) {
            blockFace = direction!!.closestBlockFace
        }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WALL_SIGN, CommonBlockProperties.FACING_DIRECTION)

    }
}
