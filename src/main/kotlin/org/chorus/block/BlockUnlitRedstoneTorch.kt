package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.event.redstone.RedstoneUpdateEvent
import org.chorus.item.*
import org.chorus.level.Level
import org.chorus.math.BlockFace
import org.chorus.utils.RedstoneComponent

class BlockUnlitRedstoneTorch @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockTorch(blockstate), RedstoneComponent {
    override val name: String
        get() = "Unlit Redstone Torch"

    override val lightLevel: Int
        get() = 0

    override fun getWeakPower(side: BlockFace): Int {
        return 0
    }

    override fun getStrongPower(side: BlockFace?): Int {
        return 0
    }

    override fun toItem(): Item {
        return ItemBlock(get(BlockID.REDSTONE_TORCH))
    }

    override fun onUpdate(type: Int): Int {
        if (super.onUpdate(type) == 0) {
            if (!Server.instance.settings.levelSettings.enableRedstone) {
                return 0
            }

            if (type == Level.BLOCK_UPDATE_NORMAL || type == Level.BLOCK_UPDATE_REDSTONE) {
                level.scheduleUpdate(this, tickRate())
            } else if (type == Level.BLOCK_UPDATE_SCHEDULED) {
                val ev = RedstoneUpdateEvent(this)
                Server.instance.pluginManager.callEvent(ev)
                if (ev.isCancelled) {
                    return 0
                }

                if (checkState()) {
                    return 1
                }
            }
        }

        return 0
    }

    private fun checkState(): Boolean {
        if (!this.isPoweredFromSide) {
            level.setBlock(this.position, get(BlockID.REDSTONE_TORCH).setPropertyValues(propertyValues), false, true)
            updateAllAroundRedstone(blockFace.getOpposite())
            return true
        }

        return false
    }

    protected val isPoweredFromSide: Boolean
        /**
         * Whether there is a power source in the opposite face of the current face
         */
        get() {
            val face = blockFace.getOpposite()
            val side = this.getSide(face)
            if (side is BlockPistonBase && side.isGettingPower) {
                return true
            }

            return level.isSidePowered(side.position, face)
        }

    override fun tickRate(): Int {
        return 2
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.UNLIT_REDSTONE_TORCH, CommonBlockProperties.TORCH_FACING_DIRECTION)

    }
}