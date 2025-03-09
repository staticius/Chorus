package org.chorus.network.process.processor

import org.chorus.AdventureSettings
import org.chorus.PlayerHandle
import org.chorus.Server
import org.chorus.block.BlockFrame
import org.chorus.block.BlockLectern
import org.chorus.entity.Entity.getServer
import org.chorus.entity.EntityHuman.getName
import org.chorus.event.player.*
import org.chorus.item.ItemID
import org.chorus.item.enchantment.Enchantment
import org.chorus.level.Sound
import org.chorus.math.BlockFace.Companion.fromIndex
import org.chorus.math.BlockVector3
import org.chorus.math.Vector3
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.MovePlayerPacket
import org.chorus.network.protocol.PlayerActionPacket
import org.chorus.network.protocol.ProtocolInfo
import lombok.extern.slf4j.Slf4j

@Slf4j
class PlayerActionProcessor : DataPacketProcessor<PlayerActionPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: PlayerActionPacket) {
        val player = playerHandle.player
        if (!player.spawned || (!player.isAlive() && pk.action != PlayerActionPacket.ACTION_RESPAWN && pk.action != PlayerActionPacket.ACTION_DIMENSION_CHANGE_ACK)) {
            return
        }

        pk.entityId = player.getId()
        val pos = Vector3(pk.x.toDouble(), pk.y.toDouble(), pk.z.toDouble())
        val face = fromIndex(pk.face)

        when (pk.action) {
            PlayerActionPacket.ACTION_START_BREAK -> {
                if (Server.getInstance().serverAuthoritativeMovement > 0) {
                    return
                }

                playerHandle.onBlockBreakStart(pos, face)
            }

            PlayerActionPacket.ACTION_ABORT_BREAK, PlayerActionPacket.ACTION_STOP_BREAK -> {
                if (Server.getInstance().serverAuthoritativeMovement > 0) {
                    return
                }

                playerHandle.onBlockBreakAbort(pos)
            }

            PlayerActionPacket.ACTION_CREATIVE_PLAYER_DESTROY_BLOCK -> {
                // Used by client to get book from lecterns and items from item frame in creative mode since 1.20.70
                val blockLectern = playerHandle.player.level!!.getBlock(pos)
                if (blockLectern is BlockLectern && blockLectern.position.distance(playerHandle.player.position) <= 6) {
                    blockLectern.dropBook(playerHandle.player)
                }
                if (blockLectern is BlockFrame && blockLectern.blockEntity != null) {
                    blockLectern.blockEntity!!.dropItem(playerHandle.player)
                }
                if (player.getServer().getServerAuthoritativeMovement() > 0) break //ServerAuthorInput not use player

                playerHandle.onBlockBreakComplete(BlockVector3(pk.x, pk.y, pk.z), face)
            }

            PlayerActionPacket.ACTION_CONTINUE_BREAK -> {
                if (Server.getInstance().serverAuthoritativeMovement > 0) {
                    return
                }

                playerHandle.onBlockBreakContinue(pos, face)
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
                playerHandle.respawn()
            }

            PlayerActionPacket.ACTION_JUMP -> {
                if (Server.getInstance().serverAuthoritativeMovement > 0) {
                    return
                }

                val playerJumpEvent = PlayerJumpEvent(player)
                player.getServer().getPluginManager().callEvent(playerJumpEvent)
            }

            PlayerActionPacket.ACTION_START_SPRINT -> {
                if (Server.getInstance().serverAuthoritativeMovement > 0) {
                    return
                }

                val playerToggleSprintEvent = PlayerToggleSprintEvent(player, true)
                player.getServer().getPluginManager().callEvent(playerToggleSprintEvent)
                if (playerToggleSprintEvent.isCancelled) {
                    player.sendData(player)
                } else {
                    player.setSprinting(true)
                }
            }

            PlayerActionPacket.ACTION_STOP_SPRINT -> {
                if (Server.getInstance().serverAuthoritativeMovement > 0) {
                    return
                }

                val playerToggleSprintEvent = PlayerToggleSprintEvent(player, false)
                player.getServer().getPluginManager().callEvent(playerToggleSprintEvent)
                if (playerToggleSprintEvent.isCancelled) {
                    player.sendData(player)
                } else {
                    player.setSprinting(false)
                }
            }

            PlayerActionPacket.ACTION_START_SNEAK -> {
                if (Server.getInstance().serverAuthoritativeMovement > 0) {
                    return
                }

                val playerToggleSneakEvent = PlayerToggleSneakEvent(player, true)
                player.getServer().getPluginManager().callEvent(playerToggleSneakEvent)
                if (playerToggleSneakEvent.isCancelled) {
                    player.sendData(player)
                } else {
                    player.setSneaking(true)
                }
            }

            PlayerActionPacket.ACTION_STOP_SNEAK -> {
                if (Server.getInstance().serverAuthoritativeMovement > 0) {
                    return
                }

                val playerToggleSneakEvent = PlayerToggleSneakEvent(player, false)
                player.getServer().getPluginManager().callEvent(playerToggleSneakEvent)
                if (playerToggleSneakEvent.isCancelled) {
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
                if (Server.getInstance().serverAuthoritativeMovement > 0) {
                    return
                }

                val playerToggleGlideEvent = PlayerToggleGlideEvent(player, true)
                player.getServer().getPluginManager().callEvent(playerToggleGlideEvent)
                if (playerToggleGlideEvent.isCancelled) {
                    player.sendData(player)
                } else {
                    player.setGliding(true)
                }
            }

            PlayerActionPacket.ACTION_STOP_GLIDE -> {
                if (Server.getInstance().serverAuthoritativeMovement > 0) {
                    return
                }

                val playerToggleGlideEvent = PlayerToggleGlideEvent(player, false)
                player.getServer().getPluginManager().callEvent(playerToggleGlideEvent)
                if (playerToggleGlideEvent.isCancelled) {
                    player.sendData(player)
                } else {
                    player.setGliding(false)
                }
            }

            PlayerActionPacket.ACTION_START_SWIMMING -> {
                if (Server.getInstance().serverAuthoritativeMovement > 0) {
                    return
                }

                val ptse = PlayerToggleSwimEvent(player, true)
                player.getServer().getPluginManager().callEvent(ptse)

                if (ptse.isCancelled) {
                    player.sendData(player)
                } else {
                    player.setSwimming(true)
                }
            }

            PlayerActionPacket.ACTION_STOP_SWIMMING -> {
                if (Server.getInstance().serverAuthoritativeMovement > 0) {
                    return
                }

                val ev = PlayerToggleSwimEvent(player, false)
                player.getServer().getPluginManager().callEvent(ev)

                if (ev.isCancelled) {
                    player.sendData(player)
                } else {
                    player.setSwimming(false)
                }
            }

            PlayerActionPacket.ACTION_START_SPIN_ATTACK -> {
                if (player.getInventory().itemInHand.id != ItemID.TRIDENT) {
                    player.sendPosition(
                        player.position,
                        player.rotation.yaw,
                        player.rotation.pitch,
                        MovePlayerPacket.MODE_RESET
                    )
                    break
                }

                val riptideLevel = player.getInventory().itemInHand.getEnchantmentLevel(Enchantment.ID_TRIDENT_RIPTIDE)
                if (riptideLevel < 1) {
                    player.sendPosition(
                        player.position,
                        player.rotation.yaw,
                        player.rotation.pitch,
                        MovePlayerPacket.MODE_RESET
                    )
                    break
                }

                if (!(player.isTouchingWater() || (player.level!!.isRaining && player.level!!.canBlockSeeSky(player.position)))) {
                    player.sendPosition(
                        player.position,
                        player.rotation.yaw,
                        player.rotation.pitch,
                        MovePlayerPacket.MODE_RESET
                    )
                    break
                }

                val playerToggleSpinAttackEvent = PlayerToggleSpinAttackEvent(player, true)
                player.getServer().getPluginManager().callEvent(playerToggleSpinAttackEvent)

                if (playerToggleSpinAttackEvent.isCancelled) {
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
                player.getServer().getPluginManager().callEvent(playerToggleSpinAttackEvent)

                if (playerToggleSpinAttackEvent.isCancelled) {
                    player.sendData(player)
                } else {
                    player.setSpinAttacking(false)
                }
            }

            PlayerActionPacket.ACTION_START_FLYING -> {
                if (Server.getInstance().serverAuthoritativeMovement > 0) {
                    return
                }

                if (!player.getServer()
                        .getAllowFlight() && !player.adventureSettings[AdventureSettings.Type.ALLOW_FLIGHT]
                ) {
                    player.kick(PlayerKickEvent.Reason.FLYING_DISABLED, "Flying is not enabled on this server")
                    break
                }
                val playerToggleFlightEvent = PlayerToggleFlightEvent(player, true)
                player.getServer().getPluginManager().callEvent(playerToggleFlightEvent)
                if (playerToggleFlightEvent.isCancelled) {
                    player.adventureSettings.update()
                } else {
                    player.adventureSettings[AdventureSettings.Type.FLYING] = playerToggleFlightEvent.isFlying
                }
            }

            PlayerActionPacket.ACTION_STOP_FLYING -> {
                if (Server.getInstance().serverAuthoritativeMovement > 0) {
                    return
                }

                val playerToggleFlightEvent = PlayerToggleFlightEvent(player, false)
                player.getServer().getPluginManager().callEvent(playerToggleFlightEvent)
                if (playerToggleFlightEvent.isCancelled) {
                    player.adventureSettings.update()
                } else {
                    player.adventureSettings[AdventureSettings.Type.FLYING] = playerToggleFlightEvent.isFlying
                }
            }

            PlayerActionPacket.ACTION_START_ITEM_USE_ON, PlayerActionPacket.ACTION_STOP_ITEM_USE_ON -> {
                // TODO
            }

            else -> PlayerActionProcessor.log.warn("{} sent invalid action id {}", player.getName(), pk.action)
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.PLAYER_ACTION_PACKET
}