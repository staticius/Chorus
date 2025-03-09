package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.type.IntPropertyType
import cn.nukkit.level.Level
import cn.nukkit.math.BlockFace
import cn.nukkit.math.BlockFace.Companion.fromIndex
import cn.nukkit.math.CompassRoseDirection

/**
 * @author PetteriM1
 */
class BlockWallBanner @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockStandingBanner(blockstate) {
    override val name: String
        get() = "Wall Banner"

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (getSide(blockFace!!.getOpposite()!!)!!.isAir) {
                level.useBreakOn(this.position)
            }
            return Level.BLOCK_UPDATE_NORMAL
        }
        return 0
    }

    override var blockFace: BlockFace?
        get() = fromIndex(getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.FACING_DIRECTION))
        set(face) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.FACING_DIRECTION, face!!.index)
        }

    override var direction: CompassRoseDirection?
        get() = blockFace!!.compassRoseDirection
        set(direction) {
            blockFace = direction!!.closestBlockFace
        }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WALL_BANNER, CommonBlockProperties.FACING_DIRECTION)
            get() = Companion.field
    }
}
