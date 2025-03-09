package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.blockentity.BlockEntityBanner.dyeColor
import cn.nukkit.blockentity.BlockEntitySign.getColor
import cn.nukkit.blockentity.BlockEntitySign.isGlowing
import cn.nukkit.blockentity.BlockEntitySign.isWaxed
import cn.nukkit.blockentity.BlockEntitySign.setColor
import cn.nukkit.blockentity.BlockEntitySign.setGlowing
import cn.nukkit.blockentity.BlockEntitySpawnable.spawnTo
import cn.nukkit.blockentity.BlockEntitySpawnable.spawnToAll
import cn.nukkit.event.Event.isCancelled
import cn.nukkit.event.player.PlayerInteractEvent
import cn.nukkit.item.Item
import cn.nukkit.item.ItemDye.dyeColor
import cn.nukkit.item.ItemTool
import cn.nukkit.math.BlockFace
import cn.nukkit.math.CompassRoseDirection.Companion.from
import cn.nukkit.math.Vector3
import cn.nukkit.utils.BlockColor.equals
import cn.nukkit.utils.DyeColor.color
import java.util.*

abstract class BlockSignBase(blockState: BlockState?) : BlockTransparent(blockState), Faceable {
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
                    position.add(0.5, 0.5, 0.5)!!,
                    LevelSoundEventPacket.SOUND_WAXED_SIGN_INTERACT_FAIL
                )
                return
            }
            val front = when (signDirection) {
                CompassRoseDirection.EAST -> face == BlockFace.EAST
                CompassRoseDirection.SOUTH -> face == BlockFace.SOUTH
                CompassRoseDirection.WEST -> face == BlockFace.WEST
                CompassRoseDirection.NORTH -> face == BlockFace.NORTH
                CompassRoseDirection.NORTH_EAST, CompassRoseDirection.NORTH_NORTH_EAST, CompassRoseDirection.EAST_NORTH_EAST -> face == BlockFace.EAST || face == BlockFace.NORTH
                CompassRoseDirection.NORTH_WEST, CompassRoseDirection.NORTH_NORTH_WEST, CompassRoseDirection.WEST_NORTH_WEST -> face == BlockFace.WEST || face == BlockFace.NORTH
                CompassRoseDirection.SOUTH_EAST, CompassRoseDirection.SOUTH_SOUTH_EAST, CompassRoseDirection.EAST_SOUTH_EAST -> face == BlockFace.EAST || face == BlockFace.SOUTH
                CompassRoseDirection.SOUTH_WEST, CompassRoseDirection.SOUTH_SOUTH_WEST, CompassRoseDirection.WEST_SOUTH_WEST -> face == BlockFace.WEST || face == BlockFace.SOUTH
            }
            if (item is ItemDye) {
                val color: BlockColor = item.dyeColor.color
                if (color.equals(blockEntity.getColor(front)) || blockEntity.isEmpty(front)) {
                    player!!.openSignEditor(this.position, front)
                    return
                }
                val event: SignColorChangeEvent = SignColorChangeEvent(this, player, color)
                level.server.pluginManager.callEvent(event)
                if (event.isCancelled) {
                    blockEntity.spawnTo(player)
                    return
                }
                blockEntity.setColor(front, color)
                blockEntity.spawnToAll()
                level.addLevelEvent(this.position, LevelEventPacket.EVENT_SOUND_DYE_USED)
                if ((player!!.gamemode and 0x01) == 0) {
                    item.count--
                }
                return
            } else if (item is ItemGlowInkSac) {
                if (blockEntity.isGlowing(front) || blockEntity.isEmpty(front)) {
                    player!!.openSignEditor(this.position, front)
                    return
                }
                val event: SignGlowEvent = SignGlowEvent(this, player, true)
                level.server.pluginManager.callEvent(event)
                if (event.isCancelled) {
                    blockEntity.spawnTo(player)
                    return
                }
                blockEntity.setGlowing(front, true)
                blockEntity.spawnToAll()
                level.addLevelEvent(this.position, LevelEventPacket.EVENT_SOUND_INK_SACE_USED)
                if ((player!!.gamemode and 0x01) == 0) {
                    item.count--
                }
                return
            } else if (item is ItemHoneycomb) {
                val event: SignWaxedEvent = SignWaxedEvent(this, player, true)
                level.server.pluginManager.callEvent(event)
                if (event.isCancelled) {
                    blockEntity.spawnTo(player)
                    return
                }
                blockEntity.isWaxed = true
                blockEntity.spawnToAll()
                level.addParticle(WaxOnParticle(this.position))
                if ((player!!.gamemode and 0x01) == 0) {
                    item.count--
                }
                return
            }
            player!!.openSignEditor(this.position, front)
        }
    }

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    open var signDirection: CompassRoseDirection?
        get() = CompassRoseDirection.from(getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.GROUND_SIGN_DIRECTION))
        set(direction) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.GROUND_SIGN_DIRECTION, direction.index)
        }

    var blockFace: BlockFace?
        get() = signDirection.closestBlockFace
        set(face) {
            signDirection = face!!.compassRoseDirection
        }

    override fun breaksWhenMoved(): Boolean {
        return true
    }

    override fun canBeActivated(): Boolean {
        return true
    }
}
