package cn.nukkit.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.entity.Entity.getServer
import cn.nukkit.entity.EntityHuman.getName
import cn.nukkit.entity.EntityRideable
import cn.nukkit.entity.custom.CustomEntity
import cn.nukkit.entity.item.EntityChestBoat
import cn.nukkit.entity.item.EntityItem
import cn.nukkit.entity.item.EntityXpOrb
import cn.nukkit.entity.mob.animal.EntityHorse
import cn.nukkit.entity.projectile.abstract_arrow.EntityArrow
import cn.nukkit.event.player.PlayerHackDetectedEvent
import cn.nukkit.event.player.PlayerKickEvent
import cn.nukkit.event.player.PlayerMouseOverEntityEvent
import cn.nukkit.lang.BaseLang.tr
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.InteractPacket
import cn.nukkit.network.protocol.ProtocolInfo
import lombok.extern.slf4j.Slf4j

@Slf4j
class InteractProcessor : DataPacketProcessor<InteractPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: InteractPacket) {
        val player = playerHandle.player
        if (!player.spawned || !player.isAlive()) {
            return
        }

        val targetEntity = player.level!!.getEntity(pk.target)

        if (targetEntity == null || !player.isAlive() || !targetEntity.isAlive()) {
            return
        }

        if (targetEntity is EntityItem || targetEntity is EntityArrow || targetEntity is EntityXpOrb) {
            // 自定义实体在客户端中可以互动, 所以不踢出玩家
            if (targetEntity is CustomEntity) {
                return
            }

            val event = PlayerHackDetectedEvent(player, PlayerHackDetectedEvent.HackType.INVALID_PVE)
            player.getServer().getPluginManager().callEvent(event)

            if (event.isKick) player.kick(
                PlayerKickEvent.Reason.INVALID_PVE,
                "Attempting to interact with an invalid entity"
            )

            InteractProcessor.log.warn(
                player.getServer().getLanguage().tr("nukkit.player.invalidEntity", player.getName())
            )
            return
        }

        when (pk.action) {
            InteractPacket.ACTION_MOUSEOVER -> {
                if (pk.target == 0L) {
                    return
                }
                player.getServer().getPluginManager().callEvent(PlayerMouseOverEntityEvent(player, targetEntity))
            }

            InteractPacket.ACTION_VEHICLE_EXIT -> {
                if (targetEntity !is EntityRideable || player.riding == null) {
                    return
                }
                (player.riding as EntityRideable).dismountEntity(player)
            }

            InteractPacket.ACTION_OPEN_INVENTORY -> {
                if (targetEntity is EntityRideable) {
                    if (targetEntity is EntityChestBoat) {
                        player.addWindow(targetEntity.getInventory())
                        return
                    } else if (targetEntity is EntityHorse) {
                        if (targetEntity.hasOwner(false) && targetEntity.getOwnerName() == player.getName()) {
                            player.addWindow(targetEntity.getInventory())
                            return
                        }
                    }
                } else if (targetEntity.getId() != player.getId()) {
                    return
                }
                if (!playerHandle.inventoryOpen) {
                    playerHandle.inventoryOpen = player.getInventory().open(player)
                }
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.INTERACT_PACKET
}
