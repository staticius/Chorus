package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.AdventureSettings
import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.entity.item.EntityBoat
import org.chorus_oss.chorus.entity.item.EntityMinecartAbstract
import org.chorus_oss.chorus.entity.mob.animal.EntityHorse
import org.chorus_oss.chorus.event.player.*
import org.chorus_oss.chorus.level.Transform
import org.chorus_oss.chorus.level.Transform.Companion.fromObject
import org.chorus_oss.chorus.math.BlockFace.Companion.fromIndex
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.ItemStackRequestPacket
import org.chorus_oss.chorus.network.protocol.PlayerAuthInputPacket
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.protocol.types.AuthInputAction
import org.chorus_oss.chorus.network.protocol.types.PlayerActionType
import kotlin.math.abs
import kotlin.math.sqrt

class PlayerAuthInputProcessor : DataPacketProcessor<PlayerAuthInputPacket>() {
    override fun handle(player: Player, pk: PlayerAuthInputPacket) {
        val player = player.player
        if (!pk.blockActionData.isEmpty()) {
            for (action in pk.blockActionData.values) {
                //hack 自从1.19.70开始，创造模式剑客户端不会发送PREDICT_DESTROY_BLOCK，但仍然发送START_DESTROY_BLOCK，过滤掉
                if (player.inventory.itemInHand.isSword && player.isCreative && action.action == PlayerActionType.START_DESTROY_BLOCK) {
                    continue
                }
                val blockPos = action.position
                val blockFace = fromIndex(action.facing)

                val lastBreakPos =
                    if (player.player.lastBlockAction == null) null else player.player.lastBlockAction!!.position
                if (lastBreakPos != null && (lastBreakPos.x != blockPos.x || lastBreakPos.y != blockPos.y || lastBreakPos.z != blockPos.z)) {
                    player.player.onBlockBreakAbort(lastBreakPos.asVector3())
                    player.player.onBlockBreakStart(blockPos.asVector3(), blockFace)
                }

                when (action.action) {
                    PlayerActionType.START_DESTROY_BLOCK, PlayerActionType.CONTINUE_DESTROY_BLOCK -> player.player.onBlockBreakStart(
                        blockPos.asVector3(),
                        blockFace
                    )

                    PlayerActionType.ABORT_DESTROY_BLOCK, PlayerActionType.STOP_DESTROY_BLOCK -> player.player.onBlockBreakAbort(
                        blockPos.asVector3()
                    )

                    PlayerActionType.PREDICT_DESTROY_BLOCK -> {
                        player.player.onBlockBreakAbort(blockPos.asVector3())
                        player.player.onBlockBreakComplete(blockPos, blockFace)
                    }

                    else -> Unit
                }
                player.player.lastBlockAction = action
            }
        }

        // As of 1.18 this is now used for sending item stack requests such as when mining a block.
        if (pk.itemStackRequest != null) {
            val dataPacketManager = player.session.dataPacketManager
            if (dataPacketManager != null) {
                val itemStackRequestPacket = ItemStackRequestPacket()
                itemStackRequestPacket.requests.add(pk.itemStackRequest!!)
                dataPacketManager.processPacket(player, itemStackRequestPacket)
            }
        }

        if (pk.inputData.contains(AuthInputAction.START_SPRINTING)) {
            val event = PlayerToggleSprintEvent(player, true)
            Server.instance.pluginManager.callEvent(event)
            if (event.cancelled) {
                player.sendData(player)
            } else {
                player.setSprinting(true)
            }
        }
        if (pk.inputData.contains(AuthInputAction.STOP_SPRINTING)) {
            val event = PlayerToggleSprintEvent(player, false)
            Server.instance.pluginManager.callEvent(event)
            if (event.cancelled) {
                player.sendData(player)
            } else {
                player.setSprinting(false)
            }
        }
        if (pk.inputData.contains(AuthInputAction.START_SNEAKING)) {
            val event = PlayerToggleSneakEvent(player, true)
            Server.instance.pluginManager.callEvent(event)
            if (event.cancelled) {
                player.sendData(player)
            } else {
                player.setSneaking(true)
            }
        }
        if (pk.inputData.contains(AuthInputAction.STOP_SNEAKING)) {
            val event = PlayerToggleSneakEvent(player, false)
            Server.instance.pluginManager.callEvent(event)
            if (event.cancelled) {
                player.sendData(player)
            } else {
                player.setSneaking(false)
            }
        }
        if (player.adventureSettings[AdventureSettings.Type.FLYING]) {
            player.isFlySneaking = pk.inputData.contains(AuthInputAction.SNEAKING)
        }
        if (pk.inputData.contains(AuthInputAction.START_JUMPING)) {
            val playerJumpEvent = PlayerJumpEvent(player)
            Server.instance.pluginManager.callEvent(playerJumpEvent)
        }
        if (pk.inputData.contains(AuthInputAction.START_SWIMMING)) {
            val playerSwimmingEvent = PlayerToggleSwimEvent(player, true)
            Server.instance.pluginManager.callEvent(playerSwimmingEvent)
            if (playerSwimmingEvent.cancelled) {
                player.sendData(player)
            } else {
                player.setSwimming(true)
            }
        }
        if (pk.inputData.contains(AuthInputAction.STOP_SWIMMING)) {
            val playerSwimmingEvent = PlayerToggleSwimEvent(player, false)
            Server.instance.pluginManager.callEvent(playerSwimmingEvent)
            if (playerSwimmingEvent.cancelled) {
                player.sendData(player)
            } else {
                player.setSwimming(false)
            }
        }
        if (pk.inputData.contains(AuthInputAction.START_GLIDING)) {
            val playerToggleGlideEvent = PlayerToggleGlideEvent(player, true)
            Server.instance.pluginManager.callEvent(playerToggleGlideEvent)
            if (playerToggleGlideEvent.cancelled) {
                player.sendData(player)
            } else {
                player.setGliding(true)
            }
        }
        if (pk.inputData.contains(AuthInputAction.STOP_GLIDING)) {
            val playerToggleGlideEvent = PlayerToggleGlideEvent(player, false)
            Server.instance.pluginManager.callEvent(playerToggleGlideEvent)
            if (playerToggleGlideEvent.cancelled) {
                player.sendData(player)
            } else {
                player.setGliding(false)
            }
        }
        if (pk.inputData.contains(AuthInputAction.START_FLYING)) {
            if (!Server.instance.allowFlight && !player.adventureSettings[AdventureSettings.Type.ALLOW_FLIGHT]
            ) {
                player.kick(PlayerKickEvent.Reason.FLYING_DISABLED, "Flying is not enabled on this server")
                return
            }
            val playerToggleFlightEvent = PlayerToggleFlightEvent(player, true)
            Server.instance.pluginManager.callEvent(playerToggleFlightEvent)
            if (playerToggleFlightEvent.cancelled) {
                player.adventureSettings.update()
            } else {
                player.adventureSettings[AdventureSettings.Type.FLYING] = playerToggleFlightEvent.isFlying
            }
        }
        if (pk.inputData.contains(AuthInputAction.STOP_FLYING)) {
            val playerToggleFlightEvent = PlayerToggleFlightEvent(player, false)
            Server.instance.pluginManager.callEvent(playerToggleFlightEvent)
            if (playerToggleFlightEvent.cancelled) {
                player.adventureSettings.update()
            } else {
                player.adventureSettings[AdventureSettings.Type.FLYING] = playerToggleFlightEvent.isFlying
            }
        }
        val clientPosition = pk.position!!.asVector3().subtract(0.0, player.player.getBaseOffset().toDouble(), 0.0)
        var yaw = pk.yaw % 360
        val pitch = pk.pitch % 360
        var headYaw = pk.headYaw % 360
        if (headYaw < 0) {
            headYaw += 360f
        }
        if (yaw < 0) {
            yaw += 360f
        }
        val clientLoc = fromObject(
            clientPosition,
            player.level!!, yaw.toDouble(), pitch.toDouble(), headYaw.toDouble()
        )
        // Proper player.isPassenger() check may be needed
        val riding = player.riding
        if (riding is EntityMinecartAbstract) {
            val inputY = pk.motion!!.y
            if (inputY >= -1.001 && inputY <= 1.001) {
                riding.setCurrentSpeed(inputY)
            }
        } else if (riding is EntityBoat && pk.inputData.contains(AuthInputAction.IN_CLIENT_PREDICTED_IN_VEHICLE)) {
            if (riding.getRuntimeID() == pk.predictedVehicle && riding.isControlling(player)) {
                if (check(clientLoc, player)) {
                    val offsetLoc = clientLoc.add(0.0, player.player.getBaseOffset().toDouble(), 0.0)
                    riding.onInput(offsetLoc)
                    player.player.handleMovement(offsetLoc)
                }
                return
            }
        } else if (riding is EntityHorse) {
            if (check(clientLoc, player)) {
                val playerLoc: Transform
                if (riding.hasOwner() && !riding.getSaddle().isNothing) {
                    riding.onInput(clientLoc.add(0.0, riding.getHeight().toDouble(), 0.0))
                    playerLoc = clientLoc.add(0.0, (player.player.getBaseOffset() + riding.getHeight()).toDouble(), 0.0)
                } else {
                    playerLoc = clientLoc.add(0.0, 0.8, 0.0)
                }
                player.player.handleMovement(playerLoc)
                return
            }
        }
        player.player.offerMovementTask(clientLoc)
    }

    override val packetId: Int
        get() = ProtocolInfo.PLAYER_AUTH_INPUT_PACKET

    companion object {
        private fun check(clientLoc: Transform, player: Player): Boolean {
            val distance = clientLoc.position.distanceSquared(player.position)
            val updatePosition = sqrt(distance).toFloat() > 0.1f
            val updateRotation =
                abs(player.rotation.pitch - clientLoc.rotation.pitch).toFloat() > 1 || abs(player.rotation.yaw - clientLoc.rotation.yaw).toFloat() > 1 || abs(
                    player.headYaw - clientLoc.headYaw
                ).toFloat() > 1
            return updatePosition || updateRotation
        }
    }
}
