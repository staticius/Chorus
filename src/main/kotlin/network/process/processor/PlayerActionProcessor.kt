package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.AdventureSettings
import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.block.BlockFrame
import org.chorus_oss.chorus.block.BlockLectern
import org.chorus_oss.chorus.event.player.*
import org.chorus_oss.chorus.item.ItemID
import org.chorus_oss.chorus.item.enchantment.Enchantment
import org.chorus_oss.chorus.level.Sound
import org.chorus_oss.chorus.math.BlockFace.Companion.fromIndex
import org.chorus_oss.chorus.math.BlockVector3
import org.chorus_oss.chorus.math.Vector3
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.MovePlayerPacket
import org.chorus_oss.chorus.network.protocol.PlayerActionPacket
import org.chorus_oss.chorus.utils.Loggable


class PlayerActionProcessor : DataPacketProcessor<PlayerActionPacket>() {
    override fun handle(player: Player, pk: PlayerActionPacket) {
        val player = player.player
        if (!player.spawned || (!player.isAlive() && pk.action != PlayerActionPacket.ACTION_RESPAWN && pk.action != PlayerActionPacket.ACTION_DIMENSION_CHANGE_ACK)) {
            return
        }

        pk.entityId = player.getRuntimeID()
        val pos = Vector3(pk.x.toDouble(), pk.y.toDouble(), pk.z.toDouble())
        val face = fromIndex(pk.face)

        run switch@{
            when (pk.action) {
                PlayerActionPacket.ACTION_START_BREAK -> {
                    if (Server.instance.getServerAuthoritativeMovement() > 0) {
                        return
                    }

                    player.player.onBlockBreakStart(pos, face)
                }

                PlayerActionPacket.ACTION_ABORT_BREAK, PlayerActionPacket.ACTION_STOP_BREAK -> {
                    if (Server.instance.getServerAuthoritativeMovement() > 0) {
                        return
                    }

                    player.player.onBlockBreakAbort(pos)
                }

                PlayerActionPacket.ACTION_CREATIVE_PLAYER_DESTROY_BLOCK -> {
                    // Used by client to get book from lecterns and items from item frame in creative mode since 1.20.70
                    val blockLectern = player.player.level!!.getBlock(pos)
                    if (blockLectern is BlockLectern && blockLectern.position.distance(player.player.position) <= 6) {
                        blockLectern.dropBook(player.player)
                    }
                    if (blockLectern is BlockFrame && blockLectern.blockEntity != null) {
                        blockLectern.blockEntity!!.dropItem(player.player)
                    }
                    if (Server.instance.getServerAuthoritativeMovement() > 0) return@switch //ServerAuthorInput not use player

                    player.player.onBlockBreakComplete(BlockVector3(pk.x, pk.y, pk.z), face)
                }

                PlayerActionPacket.ACTION_CONTINUE_BREAK -> {
                    if (Server.instance.getServerAuthoritativeMovement() > 0) {
                        return
                    }

                    player.player.onBlockBreakContinue(pos, face)
                }

                PlayerActionPacket.ACTION_GET_UPDATED_BLOCK -> {
                    //TODO
                }

                PlayerActionPacket.ACTION_DROP_ITEM -> {
                    //TODO
                }

                PlayerActionPacket.ACTION_STOP_SLEEPING -> player.stopSleep()
                PlayerActionPacket.ACTION_RESPAWN -> {
                    if (!player.spawned || player.isAlive() || !player.isOnline) {
                        return
                    }
                    player.respawn()
                }

                PlayerActionPacket.ACTION_JUMP -> {
                    if (Server.instance.getServerAuthoritativeMovement() > 0) {
                        return
                    }

                    val playerJumpEvent = PlayerJumpEvent(player)
                    Server.instance.pluginManager.callEvent(playerJumpEvent)
                }

                PlayerActionPacket.ACTION_START_SPRINT -> {
                    if (Server.instance.getServerAuthoritativeMovement() > 0) {
                        return
                    }

                    val playerToggleSprintEvent = PlayerToggleSprintEvent(player, true)
                    Server.instance.pluginManager.callEvent(playerToggleSprintEvent)
                    if (playerToggleSprintEvent.cancelled) {
                        player.sendData(player)
                    } else {
                        player.setSprinting(true)
                    }
                }

                PlayerActionPacket.ACTION_STOP_SPRINT -> {
                    if (Server.instance.getServerAuthoritativeMovement() > 0) {
                        return
                    }

                    val playerToggleSprintEvent = PlayerToggleSprintEvent(player, false)
                    Server.instance.pluginManager.callEvent(playerToggleSprintEvent)
                    if (playerToggleSprintEvent.cancelled) {
                        player.sendData(player)
                    } else {
                        player.setSprinting(false)
                    }
                }

                PlayerActionPacket.ACTION_START_SNEAK -> {
                    if (Server.instance.getServerAuthoritativeMovement() > 0) {
                        return
                    }

                    val playerToggleSneakEvent = PlayerToggleSneakEvent(player, true)
                    Server.instance.pluginManager.callEvent(playerToggleSneakEvent)
                    if (playerToggleSneakEvent.cancelled) {
                        player.sendData(player)
                    } else {
                        player.setSneaking(true)
                    }
                }

                PlayerActionPacket.ACTION_STOP_SNEAK -> {
                    if (Server.instance.getServerAuthoritativeMovement() > 0) {
                        return
                    }

                    val playerToggleSneakEvent = PlayerToggleSneakEvent(player, false)
                    Server.instance.pluginManager.callEvent(playerToggleSneakEvent)
                    if (playerToggleSneakEvent.cancelled) {
                        player.sendData(player)
                    } else {
                        player.setSneaking(false)
                    }
                }

                PlayerActionPacket.ACTION_DIMENSION_CHANGE_ACK -> player.sendPosition(
                    player.position,
                    player.rotation.yaw,
                    player.rotation.pitch,
                    MovePlayerPacket.MODE_NORMAL
                )

                PlayerActionPacket.ACTION_START_GLIDE -> {
                    if (Server.instance.getServerAuthoritativeMovement() > 0) {
                        return
                    }

                    val playerToggleGlideEvent = PlayerToggleGlideEvent(player, true)
                    Server.instance.pluginManager.callEvent(playerToggleGlideEvent)
                    if (playerToggleGlideEvent.cancelled) {
                        player.sendData(player)
                    } else {
                        player.setGliding(true)
                    }
                }

                PlayerActionPacket.ACTION_STOP_GLIDE -> {
                    if (Server.instance.getServerAuthoritativeMovement() > 0) {
                        return
                    }

                    val playerToggleGlideEvent = PlayerToggleGlideEvent(player, false)
                    Server.instance.pluginManager.callEvent(playerToggleGlideEvent)
                    if (playerToggleGlideEvent.cancelled) {
                        player.sendData(player)
                    } else {
                        player.setGliding(false)
                    }
                }

                PlayerActionPacket.ACTION_START_SWIMMING -> {
                    if (Server.instance.getServerAuthoritativeMovement() > 0) {
                        return
                    }

                    val ptse = PlayerToggleSwimEvent(player, true)
                    Server.instance.pluginManager.callEvent(ptse)

                    if (ptse.cancelled) {
                        player.sendData(player)
                    } else {
                        player.setSwimming(true)
                    }
                }

                PlayerActionPacket.ACTION_STOP_SWIMMING -> {
                    if (Server.instance.getServerAuthoritativeMovement() > 0) {
                        return
                    }

                    val ev = PlayerToggleSwimEvent(player, false)
                    Server.instance.pluginManager.callEvent(ev)

                    if (ev.cancelled) {
                        player.sendData(player)
                    } else {
                        player.setSwimming(false)
                    }
                }

                PlayerActionPacket.ACTION_START_SPIN_ATTACK -> {
                    if (player.inventory.itemInHand.id != ItemID.TRIDENT) {
                        player.sendPosition(
                            player.position,
                            player.rotation.yaw,
                            player.rotation.pitch,
                            MovePlayerPacket.MODE_RESET
                        )
                        return@switch
                    }

                    val riptideLevel =
                        player.inventory.itemInHand.getEnchantmentLevel(Enchantment.ID_TRIDENT_RIPTIDE)
                    if (riptideLevel < 1) {
                        player.sendPosition(
                            player.position,
                            player.rotation.yaw,
                            player.rotation.pitch,
                            MovePlayerPacket.MODE_RESET
                        )
                        return@switch
                    }

                    if (!(player.isTouchingWater() || (player.level!!.isRaining && player.level!!.canBlockSeeSky(player.position)))) {
                        player.sendPosition(
                            player.position,
                            player.rotation.yaw,
                            player.rotation.pitch,
                            MovePlayerPacket.MODE_RESET
                        )
                        return@switch
                    }

                    val playerToggleSpinAttackEvent = PlayerToggleSpinAttackEvent(player, true)
                    Server.instance.pluginManager.callEvent(playerToggleSpinAttackEvent)

                    if (playerToggleSpinAttackEvent.cancelled) {
                        player.sendPosition(
                            player.position,
                            player.rotation.yaw,
                            player.rotation.pitch,
                            MovePlayerPacket.MODE_RESET
                        )
                    } else {
                        player.setSpinAttacking(true)
                        val riptideSound = if (riptideLevel >= 3) {
                            Sound.ITEM_TRIDENT_RIPTIDE_3
                        } else if (riptideLevel == 2) {
                            Sound.ITEM_TRIDENT_RIPTIDE_2
                        } else {
                            Sound.ITEM_TRIDENT_RIPTIDE_1
                        }
                        player.level!!.addSound(player.position, riptideSound)
                    }
                }

                PlayerActionPacket.ACTION_STOP_SPIN_ATTACK -> {
                    val playerToggleSpinAttackEvent = PlayerToggleSpinAttackEvent(player, false)
                    Server.instance.pluginManager.callEvent(playerToggleSpinAttackEvent)

                    if (playerToggleSpinAttackEvent.cancelled) {
                        player.sendData(player)
                    } else {
                        player.setSpinAttacking(false)
                    }
                }

                PlayerActionPacket.ACTION_START_FLYING -> {
                    if (Server.instance.getServerAuthoritativeMovement() > 0) {
                        return
                    }

                    if (!Server.instance.allowFlight && !player.adventureSettings[AdventureSettings.Type.ALLOW_FLIGHT]
                    ) {
                        player.kick(PlayerKickEvent.Reason.FLYING_DISABLED, "Flying is not enabled on this server")
                        return@switch
                    }
                    val playerToggleFlightEvent = PlayerToggleFlightEvent(player, true)
                    Server.instance.pluginManager.callEvent(playerToggleFlightEvent)
                    if (playerToggleFlightEvent.cancelled) {
                        player.adventureSettings.update()
                    } else {
                        player.adventureSettings[AdventureSettings.Type.FLYING] = playerToggleFlightEvent.isFlying
                    }
                }

                PlayerActionPacket.ACTION_STOP_FLYING -> {
                    if (Server.instance.getServerAuthoritativeMovement() > 0) {
                        return
                    }

                    val playerToggleFlightEvent = PlayerToggleFlightEvent(player, false)
                    Server.instance.pluginManager.callEvent(playerToggleFlightEvent)
                    if (playerToggleFlightEvent.cancelled) {
                        player.adventureSettings.update()
                    } else {
                        player.adventureSettings[AdventureSettings.Type.FLYING] = playerToggleFlightEvent.isFlying
                    }
                }

                PlayerActionPacket.ACTION_START_ITEM_USE_ON, PlayerActionPacket.ACTION_STOP_ITEM_USE_ON -> {
                    // TODO
                }

                else -> log.warn(
                    "{} sent invalid action id {}",
                    player.getEntityName(),
                    pk.action
                )
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.PLAYER_ACTION_PACKET

    companion object : Loggable
}