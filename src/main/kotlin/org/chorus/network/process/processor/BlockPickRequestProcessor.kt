package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.Server
import org.chorus.event.player.PlayerBlockPickEvent
import org.chorus.inventory.HumanInventory
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.BlockPickRequestPacket
import org.chorus.network.protocol.ProtocolInfo
import org.chorus.utils.Loggable


class BlockPickRequestProcessor : DataPacketProcessor<BlockPickRequestPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: BlockPickRequestPacket) {
        val player = playerHandle.player
        val block = player.level!!.getBlock(pk.position.asVector3(), false)
        if (block.position.distanceSquared(player.position) > 1000) {
            BlockPickRequestProcessor.log.debug(playerHandle.username + ": Block pick request for a block too far away")
            return
        }
        val item = block.toItem()

        if (pk.withData) {
            val blockEntity = player.level!!.getBlockEntity(pk.position.asVector3())
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
                player.getEntityName()
            )
            pickEvent.setCancelled()
        }

        Server.instance.pluginManager.callEvent(pickEvent)

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
                if (player.getInventory().getItem(slot).isNothing) {
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
                        if (player.getInventory().getItem(slot).isNothing) {
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

    companion object : Loggable
}
