package cn.nukkit.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.entity.EntityHuman.getName
import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.inventory.HumanInventory
import cn.nukkit.item.Item.Companion.get
import cn.nukkit.item.enchantment.Enchantment.Companion.getEnchantments
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.MobEquipmentPacket
import cn.nukkit.network.protocol.ProtocolInfo
import lombok.extern.slf4j.Slf4j

@Slf4j
class MobEquipmentProcessor : DataPacketProcessor<MobEquipmentPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: MobEquipmentPacket) {
        val player = playerHandle.player
        if (!player.spawned || !player.isAlive()) {
            return
        }

        if (pk.selectedSlot < 0 || pk.selectedSlot > 8) {
            player.close("§cPacket handling error")
            return
        }
        if (!pk.item.isNull) {
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
            MobEquipmentProcessor.log.debug(
                "Player {} has no open container with window ID {}",
                player.getName(),
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
                MobEquipmentProcessor.log.debug("Tried to equip {} but have {} in target slot", pk.item, fixItem)
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
}
