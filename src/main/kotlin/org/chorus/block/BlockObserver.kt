package org.chorus.block

import org.chorus.Player
import org.chorus.Server
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.BooleanPropertyType
import org.chorus.event.block.BlockRedstoneEvent
import org.chorus.event.redstone.RedstoneUpdateEvent
import org.chorus.item.Item
import org.chorus.item.ItemTool
import org.chorus.level.Level
import org.chorus.math.BlockFace
import org.chorus.utils.Faceable
import org.chorus.utils.RedstoneComponent
import org.chorus.utils.RedstoneComponent.Companion.updateAroundRedstone
import kotlin.math.abs

/**
 * @author Leonidius20, joserobjr
 * @since 18.08.18
 */
class BlockObserver @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockstate), RedstoneComponent, Faceable {
    override val name: String
        get() = "Observer"

    override fun place(
        item: Item,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player?
    ): Boolean {
        if (player != null) {
            if (abs(player.position.floorX - position.x) <= 1 && abs(player.position.floorZ - position.z) <= 1) {
                val y = player.position.y + player.getEyeHeight()
                blockFace = if (y - position.y > 2) {
                    BlockFace.DOWN
                } else if (position.y - y > 0) {
                    BlockFace.UP
                } else {
                    player.getHorizontalFacing()
                }
            } else {
                blockFace = player.getHorizontalFacing()
            }
        }

        level.setBlock(block.position, this, true, true)
        return true
    }

    override val isPowerSource: Boolean
        get() = true

    override fun getStrongPower(side: BlockFace?): Int {
        return if (isPowered && side == blockFace) 15 else 0
    }

    override fun getWeakPower(face: BlockFace?): Int {
        return getStrongPower(face)
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_SCHEDULED || type == Level.BLOCK_UPDATE_MOVED) {
            val ev = RedstoneUpdateEvent(this)
            val pluginManager = Server.instance.pluginManager
            pluginManager.callEvent(ev)
            if (ev.isCancelled) {
                return 0
            }

            if (!isPowered) {
                Server.instance.pluginManager.callEvent(BlockRedstoneEvent(this, 0, 15))
                isPowered = true

                if (level.setBlock(this.position, this)) {
                    getSide(blockFace!!.getOpposite()!!)!!.onUpdate(Level.BLOCK_UPDATE_REDSTONE)
                    updateAroundRedstone(getSide(blockFace!!.getOpposite()!!)!!)
                    level.scheduleUpdate(this, 2)
                }
            } else {
                pluginManager.callEvent(BlockRedstoneEvent(this, 15, 0))
                isPowered = false

                level.setBlock(this.position, this)
                getSide(blockFace!!.getOpposite()!!)!!.onUpdate(Level.BLOCK_UPDATE_REDSTONE)
                updateAroundRedstone(getSide(blockFace!!.getOpposite()!!)!!)
            }
            return type
        }
        return 0
    }

    override fun onNeighborChange(side: BlockFace) {
        val server = Server.instance
        val blockFace = blockFace
        if (!server.settings.levelSettings().enableRedstone() || side != blockFace || level.isUpdateScheduled(
                this.position,
                this
            )
        ) {
            return
        }

        val ev = RedstoneUpdateEvent(this)
        server.pluginManager.callEvent(ev)
        if (ev.isCancelled) {
            return
        }

        level.scheduleUpdate(this, 1)
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val hardness: Double
        get() = 3.5

    override val resistance: Double
        get() = 17.5

    var isPowered: Boolean
        get() = getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.POWERED_BIT)
        set(powered) {
            setPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.POWERED_BIT, powered)
        }

    override var blockFace: BlockFace?
        get() = getPropertyValue(CommonBlockProperties.MINECRAFT_FACING_DIRECTION)
        set(face) {
            setPropertyValue(
                CommonBlockProperties.MINECRAFT_FACING_DIRECTION,
                face
            )
        }

    companion object {
        val properties: BlockProperties = BlockProperties(
BlockID.BlockID.OBSERVER,
            CommonBlockProperties.MINECRAFT_FACING_DIRECTION,
            CommonBlockProperties.POWERED_BIT
        )

    }
}
