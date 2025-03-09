package cn.nukkit.block

import cn.nukkit.block.Block.waterloggingLevel
import cn.nukkit.entity.Entity
import cn.nukkit.event.Event.isCancelled
import cn.nukkit.item.ItemTool
import cn.nukkit.level.Level

/**
 * @author Pub4Game
 * @since 27.12.2015
 */
class BlockSoulSand @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockSolid(blockstate) {
    override val name: String
        get() = "Soul Sand"

    override val hardness: Double
        get() = 0.5

    override val resistance: Double
        get() = 2.5

    override val toolType: Int
        get() = ItemTool.TYPE_SHOVEL

    override var maxY: Double
        get() = position.y + 1
        set(maxY) {
            super.maxY = maxY
        }

    override fun hasEntityCollision(): Boolean {
        return true
    }

    override val isSoulSpeedCompatible: Boolean
        get() = true

    override fun onEntityCollide(entity: Entity) {
        entity.motion.x *= 0.4
        entity.motion.z *= 0.4
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            val up = up()
            if (up is BlockFlowingWater && (up.liquidDepth == 0 || up.liquidDepth == 8)) {
                val event: BlockFormEvent = BlockFormEvent(up, BlockBubbleColumn())
                if (!event.isCancelled) {
                    if (event.newState.waterloggingLevel > 0) {
                        level.setBlock(up.position, 1, BlockFlowingWater(), true, false)
                    }
                    level.setBlock(up.position, 0, event.newState, true, true)
                }
            }
        }
        return 0
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SOUL_SAND)
            get() = Companion.field
    }
}
