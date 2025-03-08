package cn.nukkit.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.entity.Entity.getServer
import cn.nukkit.entity.EntityHuman.getName
import cn.nukkit.event.player.PlayerBlockPickEvent
import cn.nukkit.inventory.HumanInventory
import cn.nukkit.math.Vector3
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.BlockPickRequestPacket
import cn.nukkit.network.protocol.ProtocolInfo
import lombok.extern.slf4j.Slf4j

@Slf4j
class BlockPickRequestProcessor : DataPacketProcessor<BlockPickRequestPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: BlockPickRequestPacket) {
        val player = playerHandle.player
        val block = player.level!!.getBlock(pk.x, pk.y, pk.z, false)
        if (block!!.position.distanceSquared(player.position) > 1000) {
            BlockPickRequestProcessor.log.debug(playerHandle.username + ": Block pick request for a block too far away")
            return
        }
        val item = block.toItem()

        if (pk.addUserData) {
            val blockEntity = player.level!!.getBlockEntity(Vector3(pk.x.toDouble(), pk.y.toDouble(), pk.z.toDouble()))
            if (blockEntity != null) {
                val nbt = blockEntity.cleanedNBT
                if (nbt != null) {
                    item.setCustomBlockData(nbt)
                    item.setLore("+(DATA)")
                }
            }
        }

        val pickEvent = PlayerBlockPickEvent(player, block, item)
        if (player.isSpectator) {
            BlockPickRequestProcessor.log.debug(
                "Got block-pick request from {} when in spectator mode",
                player.getName()
            )
            pickEvent.setCancelled()
        }

        player.getServer().getPluginManager().callEvent(pickEvent)

        if (!pickEvent.isCancelled) {
            var itemExists = false
            var itemSlot = -1
            for (slot in 0..<player.getInventory().size) {
                if (player.getInventory().getItem(slot).equals(pickEvent.item)) {
                    if (slot < player.getInventory().hotbarSize) {
                        player.getInventory().setHeldItemSlot(slot)
                    } else {
                        itemSlot = slot
                    }
                    itemExists = true
                    break
                }
            }

            for (slot in 0..<player.getInventory().hotbarSize) {
                if (player.getInventory().getItem(slot).isNull) {
                    if (!itemExists && player.isCreative) {
                        player.getInventory().setHeldItemSlot(slot)
                        player.getInventory().setItemInHand(pickEvent.item)
                        return
                    } else if (itemSlot > -1) {
                        player.getInventory().setHeldItemSlot(slot)
                        player.getInventory().setItemInHand(player.getInventory().getItem(itemSlot))
                        player.getInventory().clear(itemSlot, true)
                        return
                    }
                }
            }

            if (!itemExists && player.isCreative) {
                val itemInHand = player.getInventory().itemInHand
                player.getInventory().setItemInHand(pickEvent.item)
                if (!player.getInventory().isFull) {
                    for (slot in 0..<HumanInventory.ARMORS_INDEX) {
                        if (player.getInventory().getItem(slot).isNull) {
                            player.getInventory().setItem(slot, itemInHand)
                            break
                        }
                    }
                }
            } else if (itemSlot > -1) {
                val itemInHand = player.getInventory().itemInHand
                player.getInventory().setItemInHand(player.getInventory().getItem(itemSlot))
                player.getInventory().setItem(itemSlot, itemInHand)
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.BLOCK_PICK_REQUEST_PACKET
}
