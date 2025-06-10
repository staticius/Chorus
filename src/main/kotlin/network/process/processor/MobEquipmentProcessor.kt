package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.entity.data.EntityFlag
import org.chorus_oss.chorus.inventory.HumanInventory
import org.chorus_oss.chorus.item.Item.Companion.get
import org.chorus_oss.chorus.item.enchantment.Enchantment.Companion.getEnchantments
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.MobEquipmentPacket
import org.chorus_oss.chorus.network.protocol.ProtocolInfo
import org.chorus_oss.chorus.utils.Loggable


class MobEquipmentProcessor : DataPacketProcessor<MobEquipmentPacket>() {
    override fun handle(player: Player, pk: MobEquipmentPacket) {
        val player = player.player
        if (!player.spawned || !player.isAlive()) {
            return
        }

        if (pk.selectedSlot < 0 || pk.selectedSlot > 8) {
            player.close("§cPacket handling error")
            return
        }
        if (!pk.item.isNothing) {
            if (pk.item.enchantments.size > getEnchantments().size) { // Last Enchant Id
                player.close("§cPacket handling error")
                return
            }
            if (pk.item.lore.size > 100) {
                player.close("§cPacket handling error")
                return
            }
            if (pk.item.canPlaceOn.size() > 250) {
                player.close("§cPacket handling error")
                return
            }
            if (pk.item.canDestroy.size() > 250) {
                player.close("§cPacket handling error")
                return
            }
        }

        val inv = player.getWindowById(pk.containerId)

        if (inv == null) {
            log.debug(
                "Player {} has no open container with window ID {}",
                player.getEntityName(),
                pk.containerId
            )
            return
        }

        if (inv is HumanInventory && inv.heldItemIndex == pk.selectedSlot) {
            return
        }

        val item = inv.getItem(pk.selectedSlot)

        if (!item.equals(pk.item, false, true)) {
            val fixItem = get(item.id, item.damage, item.getCount(), item.compoundTag)
            if (fixItem.equals(pk.item, false, true)) {
                inv.setItem(pk.selectedSlot, fixItem)
            } else {
                log.debug("Tried to equip {} but have {} in target slot", pk.item, fixItem)
                inv.sendContents(player)
            }
        }

        if (inv is HumanInventory) {
            inv.equipItem(pk.selectedSlot)
        }

        player.setDataFlag(EntityFlag.USING_ITEM, false)
    }

    override val packetId: Int
        get() = ProtocolInfo.MOB_EQUIPMENT_PACKET

    companion object : Loggable
}
