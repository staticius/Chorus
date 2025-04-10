package org.chorus.network.process.processor

import org.chorus.AdventureSettings
import org.chorus.Player
import org.chorus.PlayerHandle
import org.chorus.Server
import org.chorus.entity.item.EntityBoat
import org.chorus.entity.item.EntityMinecartAbstract
import org.chorus.entity.mob.animal.EntityHorse
import org.chorus.event.player.*
import org.chorus.level.Transform
import org.chorus.level.Transform.Companion.fromObject
import org.chorus.math.BlockFace.Companion.fromIndex
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.ItemStackRequestPacket
import org.chorus.network.protocol.PlayerAuthInputPacket
import org.chorus.network.protocol.ProtocolInfo
import org.chorus.network.protocol.types.AuthInputAction
import org.chorus.network.protocol.types.PlayerActionType
import kotlin.math.abs
import kotlin.math.sqrt

class PlayerAuthInputProcessor : DataPacketProcessor<PlayerAuthInputPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: PlayerAuthInputPacket) {
        val player = playerHandle.player
        if (!pk.blockActionData.isEmpty()) {
            for (action in pk.blockActionData.values) {
                //hack 自从1.19.70开始，创造模式剑客户端不会发送PREDICT_DESTROY_BLOCK，但仍然发送START_DESTROY_BLOCK，过滤掉
                if (player.getInventory().itemInHand.isSword && player.isCreative && action.action == PlayerActionType.START_DESTROY_BLOCK) {
                    continue
                }
                val blockPos = action.position
                val blockFace = fromIndex(action.facing)

                val lastBreakPos =
                    if (playerHandle.lastBlockAction == null) null else playerHandle.lastBlockAction!!.position
                if (lastBreakPos != null && (lastBreakPos.x != blockPos.x || lastBreakPos.y != blockPos.y || lastBreakPos.z != blockPos.z)) {
                    playerHandle.onBlockBreakAbort(lastBreakPos.asVector3())
                    playerHandle.onBlockBreakStart(blockPos.asVector3(), blockFace)
                }

                when (action.action) {
                    PlayerActionType.START_DESTROY_BLOCK, PlayerActionType.CONTINUE_DESTROY_BLOCK -> playerHandle.onBlockBreakStart(
                        blockPos.asVector3(),
                        blockFace
                    )

                    PlayerActionType.ABORT_DESTROY_BLOCK, PlayerActionType.STOP_DESTROY_BLOCK -> playerHandle.onBlockBreakAbort(
                        blockPos.asVector3()
                    )

                    PlayerActionType.PREDICT_DESTROY_BLOCK -> {
                        playerHandle.onBlockBreakAbort(blockPos.asVector3())
                        playerHandle.onBlockBreakComplete(blockPos, blockFace)
                    }

                    else -> Unit
                }
                playerHandle.lastBlockAction = action
            }
        }

        // As of 1.18 this is now used for sending item stack requests such as when mining a block.
        if (pk.itemStackRequest != null) {
            val dataPacketManager = player.session.dataPacketManager
            if (dataPacketManager != null) {
                val itemStackRequestPacket = ItemStackRequestPacket()
                itemStackRequestPacket.requests.add(pk.itemStackRequest!!)
                dataPacketManager.processPacket(playerHandle, itemStackRequestPacket)
            }
        }

        if (pk.inputData.contains(AuthInputAction.START_SPRINTING)) {
            val event = PlayerToggleSprintEvent(player, true)
            Server.instance.pluginManager.callEvent(event)
            if (event.isCancelled) {
                player.sendData(player)
            } else {
                player.setSprinting(true)
            }
        }
        if (pk.inputData.contains(AuthInputAction.STOP_SPRINTING)) {
            val event = PlayerToggleSprintEvent(player, false)
            Server.instance.pluginManager.callEvent(event)
            if (event.isCancelled) {
                player.sendData(player)
            } else {
                player.setSprinting(false)
            }
        }
        if (pk.inputData.contains(AuthInputAction.START_SNEAKING)) {
            val event = PlayerToggleSneakEvent(player, true)
            Server.instance.pluginManager.callEvent(event)
            if (event.isCancelled) {
                player.sendData(player)
            } else {
                player.setSneaking(true)
            }
        }
        if (pk.inputData.contains(AuthInputAction.STOP_SNEAKING)) {
            val event = PlayerToggleSneakEvent(player, false)
            Server.instance.pluginManager.callEvent(event)
            if (event.isCancelled) {
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
            if (playerSwimmingEvent.isCancelled) {
                player.sendData(player)
            } else {
                player.setSwimming(true)
            }
        }
        if (pk.inputData.contains(AuthInputAction.STOP_SWIMMING)) {
            val playerSwimmingEvent = PlayerToggleSwimEvent(player, false)
            Server.instance.pluginManager.callEvent(playerSwimmingEvent)
            if (playerSwimmingEvent.isCancelled) {
                player.sendData(player)
            } else {
                player.setSwimming(false)
            }
        }
        if (pk.inputData.contains(AuthInputAction.START_GLIDING)) {
            val playerToggleGlideEvent = PlayerToggleGlideEvent(player, true)
            Server.instance.pluginManager.callEvent(playerToggleGlideEvent)
            if (playerToggleGlideEvent.isCancelled) {
                player.sendData(player)
            } else {
                player.setGliding(true)
            }
        }
        if (pk.inputData.contains(AuthInputAction.STOP_GLIDING)) {
            val playerToggleGlideEvent = PlayerToggleGlideEvent(player, false)
            Server.instance.pluginManager.callEvent(playerToggleGlideEvent)
            if (playerToggleGlideEvent.isCancelled) {
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
            if (playerToggleFlightEvent.isCancelled) {
                player.adventureSettings.update()
            } else {
                player.adventureSettings[AdventureSettings.Type.FLYING] = playerToggleFlightEvent.isFlying
            }
        }
        if (pk.inputData.contains(AuthInputAction.STOP_FLYING)) {
            val playerToggleFlightEvent = PlayerToggleFlightEvent(player, false)
            Server.instance.pluginManager.callEvent(playerToggleFlightEvent)
            if (playerToggleFlightEvent.isCancelled) {
                player.adventureSettings.update()
            } else {
                player.adventureSettings[AdventureSettings.Type.FLYING] = playerToggleFlightEvent.isFlying
            }
        }
        val clientPosition = pk.position!!.asVector3().subtract(0.0, playerHandle.baseOffset.toDouble(), 0.0)
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
                    val offsetLoc = clientLoc.add(0.0, playerHandle.baseOffset.toDouble(), 0.0)
                    riding.onInput(offsetLoc)
                    playerHandle.handleMovement(offsetLoc)
                }
                return
            }
        } else if (riding is EntityHorse) {
            if (check(clientLoc, player)) {
                val playerLoc: Transform
                if (riding.hasOwner() && !riding.getSaddle().isNothing) {
                    riding.onInput(clientLoc.add(0.0, riding.getHeight().toDouble(), 0.0))
                    playerLoc = clientLoc.add(0.0, (playerHandle.baseOffset + riding.getHeight()).toDouble(), 0.0)
                } else {
                    playerLoc = clientLoc.add(0.0, 0.8, 0.0)
                }
                playerHandle.handleMovement(playerLoc)
                return
            }
        }
        playerHandle.offerMovementTask(clientLoc)
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
