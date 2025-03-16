package org.chorus.block

import org.chorus.Player
import org.chorus.Server
import org.chorus.block.property.CommonBlockProperties
import org.chorus.blockentity.BlockEntitySign
import org.chorus.event.block.SignColorChangeEvent
import org.chorus.event.block.SignGlowEvent
import org.chorus.event.block.SignWaxedEvent
import org.chorus.event.player.PlayerInteractEvent
import org.chorus.item.*
import org.chorus.level.particle.WaxOnParticle
import org.chorus.math.BlockFace
import org.chorus.math.CompassRoseDirection
import org.chorus.math.Vector3
import org.chorus.network.protocol.LevelEventPacket
import org.chorus.network.protocol.LevelSoundEventPacket
import org.chorus.utils.BlockColor
import org.chorus.utils.Faceable
import java.util.*

abstract class BlockSignBase(blockState: BlockState) : BlockTransparent(blockState), Faceable {
    override val hardness: Double
        get() = 1.0

    override val resistance: Double
        get() = 5.0

    override val isSolid: Boolean
        get() = false

    override fun isSolid(side: BlockFace): Boolean {
        return false
    }

    override val waterloggingLevel: Int
        get() = 1

    override fun onTouch(
        vector: Vector3,
        item: Item,
        face: BlockFace,
        fx: Float,
        fy: Float,
        fz: Float,
        player: Player?,
        action: PlayerInteractEvent.Action
    ) {
        if (action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            val blockEntity =
                level.getBlockEntity(this.position) as? BlockEntitySign ?: return
            // If a sign is waxed, it cannot be modified.
            if (blockEntity.isWaxed || (Objects.requireNonNull<Player?>(player)
                    .isSneaking() && item.id != BlockID.AIR)
            ) {
                level.addLevelSoundEvent(
                    position.add(0.5, 0.5, 0.5),
                    LevelSoundEventPacket.SOUND_WAXED_SIGN_INTERACT_FAIL
                )
                return
            }
            val front = when (getSignDirection()) {
                CompassRoseDirection.EAST -> face == BlockFace.EAST
                CompassRoseDirection.SOUTH -> face == BlockFace.SOUTH
                CompassRoseDirection.WEST -> face == BlockFace.WEST
                CompassRoseDirection.NORTH -> face == BlockFace.NORTH
                CompassRoseDirection.NORTH_EAST, CompassRoseDirection.NORTH_NORTH_EAST, CompassRoseDirection.EAST_NORTH_EAST -> face == BlockFace.EAST || face == BlockFace.NORTH
                CompassRoseDirection.NORTH_WEST, CompassRoseDirection.NORTH_NORTH_WEST, CompassRoseDirection.WEST_NORTH_WEST -> face == BlockFace.WEST || face == BlockFace.NORTH
                CompassRoseDirection.SOUTH_EAST, CompassRoseDirection.SOUTH_SOUTH_EAST, CompassRoseDirection.EAST_SOUTH_EAST -> face == BlockFace.EAST || face == BlockFace.SOUTH
                CompassRoseDirection.SOUTH_WEST, CompassRoseDirection.SOUTH_SOUTH_WEST, CompassRoseDirection.WEST_SOUTH_WEST -> face == BlockFace.WEST || face == BlockFace.SOUTH
            }

            when (item) {
                is ItemDye -> {
                    val color: BlockColor = item.dyeColor.color!!
                    if (color == blockEntity.getColor(front) || blockEntity.isEmpty(front)) {
                        player!!.openSignEditor(this.position, front)
                        return
                    }
                    val event = SignColorChangeEvent(this, player!!, color)
                    Server.instance.pluginManager.callEvent(event)
                    if (event.isCancelled) {
                        blockEntity.spawnTo(player)
                        return
                    }
                    blockEntity.setColor(front, color)
                    blockEntity.spawnToAll()
                    level.addLevelEvent(this.position, LevelEventPacket.EVENT_SOUND_DYE_USED)
                    if ((player.gamemode and 0x01) == 0) {
                        item.count--
                    }
                    return
                }

                is ItemGlowInkSac -> {
                    if (blockEntity.isGlowing(front) || blockEntity.isEmpty(front)) {
                        player!!.openSignEditor(this.position, front)
                        return
                    }
                    val event = SignGlowEvent(this, player!!, true)
                    Server.instance.pluginManager.callEvent(event)
                    if (event.isCancelled) {
                        blockEntity.spawnTo(player)
                        return
                    }
                    blockEntity.setGlowing(front, true)
                    blockEntity.spawnToAll()
                    level.addLevelEvent(this.position, LevelEventPacket.EVENT_SOUND_INK_SACE_USED)
                    if ((player.gamemode and 0x01) == 0) {
                        item.count--
                    }
                    return
                }

                is ItemHoneycomb -> {
                    val event = SignWaxedEvent(this, player!!, true)
                    Server.instance.pluginManager.callEvent(event)
                    if (event.isCancelled) {
                        blockEntity.spawnTo(player)
                        return
                    }
                    blockEntity.isWaxed = true
                    blockEntity.spawnToAll()
                    level.addParticle(WaxOnParticle(this.position))
                    if ((player.gamemode and 0x01) == 0) {
                        item.count--
                    }
                    return
                }

                else -> player!!.openSignEditor(this.position, front)
            }
        }
    }

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    open fun getSignDirection(): CompassRoseDirection =
        CompassRoseDirection.from(getPropertyValue(CommonBlockProperties.GROUND_SIGN_DIRECTION))

    open fun setSignDirection(direction: CompassRoseDirection) =
        setPropertyValue(CommonBlockProperties.GROUND_SIGN_DIRECTION, direction.index)

    override var blockFace: BlockFace
        get() = getSignDirection().closestBlockFace
        set(face) {
            setSignDirection(face!!.compassRoseDirection!!)
        }

    override fun breaksWhenMoved(): Boolean {
        return true
    }

    override fun canBeActivated(): Boolean {
        return true
    }
}
