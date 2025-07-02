package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.Server
import org.chorus_oss.chorus.event.player.PlayerBlockPickEvent
import org.chorus_oss.chorus.inventory.HumanInventory
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.BlockPickRequestPacket
import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.utils.Loggable


class BlockPickRequestProcessor : DataPacketProcessor<BlockPickRequestPacket>() {
    override fun handle(player: Player, pk: BlockPickRequestPacket) {
        val player = player.player
        val block = player.level!!.getBlock(pk.position.asVector3(), false)
        if (block.position.distanceSquared(player.position) > 1000) {
            log.debug(player.player.getEntityName() + ": Block pick request for a block too far away")
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
            log.debug(
                "Got block-pick request from {} when in spectator mode",
                player.getEntityName()
            )
            pickEvent.cancelled = true
        }

        Server.instance.pluginManager.callEvent(pickEvent)

        if (!pickEvent.cancelled) {
            var itemExists = false
            var itemSlot = -1
            for (slot in 0..<player.inventory.size) {
                if (player.inventory.getItem(slot).equals(pickEvent.item)) {
                    if (slot < player.inventory.hotbarSize) {
                        player.inventory.setHeldItemSlot(slot)
                    } else {
                        itemSlot = slot
                    }
                    itemExists = true
                    break
                }
            }

            for (slot in 0..<player.inventory.hotbarSize) {
                if (player.inventory.getItem(slot).isNothing) {
                    if (!itemExists && player.isCreative) {
                        player.inventory.setHeldItemSlot(slot)
                        player.inventory.setItemInHand(pickEvent.item)
                        return
                    } else if (itemSlot > -1) {
                        player.inventory.setHeldItemSlot(slot)
                        player.inventory.setItemInHand(player.inventory.getItem(itemSlot))
                        player.inventory.clear(itemSlot, true)
                        return
                    }
                }
            }

            if (!itemExists && player.isCreative) {
                val itemInHand = player.inventory.itemInHand
                player.inventory.setItemInHand(pickEvent.item)
                if (!player.inventory.isFull) {
                    for (slot in 0..<HumanInventory.ARMORS_INDEX) {
                        if (player.inventory.getItem(slot).isNothing) {
                            player.inventory.setItem(slot, itemInHand)
                            break
                        }
                    }
                }
            } else if (itemSlot > -1) {
                val itemInHand = player.inventory.itemInHand
                player.inventory.setItemInHand(player.inventory.getItem(itemSlot))
                player.inventory.setItem(itemSlot, itemInHand)
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.BLOCK_PICK_REQUEST_PACKET

    companion object : Loggable
}
