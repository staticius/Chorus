package org.chorus.block

import cn.nukkit.event.Event.isCancelled
import cn.nukkit.level.*
import cn.nukkit.math.BlockFace
import java.util.concurrent.ThreadLocalRandom

open class BlockBubbleCoral @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCoral(blockstate) {
    override fun isDead(): Boolean {
        return false
    }

    override fun getDeadCoral(): Block {
        return BlockDeadBubbleCoral()
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (this.id == BUBBLE_CORAL) {
                level.scheduleUpdate(this, 60 + ThreadLocalRandom.current().nextInt(40))
            }
            return type
        } else if (type == Level.BLOCK_UPDATE_SCHEDULED) {
            for (face in BlockFace.entries) {
                if (getSideAtLayer(0, face) is BlockFlowingWater ||
                    getSideAtLayer(1, face) is BlockFlowingWater ||
                    getSideAtLayer(0, face) is BlockFrostedIce || getSideAtLayer(1, face) is BlockFrostedIce
                ) {
                    return type
                }
            }
            val event: BlockFadeEvent = BlockFadeEvent(this, deadCoral)
            if (!event.isCancelled) {
                level.setBlock(this.position, event.newState, true, true)
            }
            return type
        }
        return 0
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BUBBLE_CORAL)
            get() = Companion.field
    }
}