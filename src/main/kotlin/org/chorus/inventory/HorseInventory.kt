package org.chorus.inventory

import org.chorus.Player
import org.chorus.Server
import org.chorus.entity.Entity.getId
import org.chorus.entity.Entity.setDataFlag
import org.chorus.entity.data.EntityFlag
import org.chorus.entity.mob.animal.EntityHorse
import org.chorus.item.*
import org.chorus.level.Sound
import org.chorus.nbt.NBTIO
import org.chorus.nbt.tag.CompoundTag
import org.chorus.nbt.tag.ListTag
import org.chorus.network.protocol.ContainerClosePacket
import org.chorus.network.protocol.LevelSoundEventPacket
import org.chorus.network.protocol.MobArmorEquipmentPacket
import org.chorus.network.protocol.UpdateEquipmentPacket
import org.chorus.network.protocol.types.itemstack.ContainerSlotType
import java.io.IOException
import java.util.List

class HorseInventory(holder: EntityHorse?) : BaseInventory(holder, InventoryType.HORSE, 2) {
    var saddle: Item?
        get() = this.getItem(0)
        set(item) {
            this.setItem(0, item!!)
        }

    var horseArmor: Item?
        get() = this.getItem(1)
        set(item) {
            this.setItem(1, item!!)
        }

    override fun init() {
        slotTypeMap[0] = ContainerSlotType.HORSE_EQUIP
        slotTypeMap[1] = ContainerSlotType.HORSE_EQUIP
    }

    override fun onSlotChange(index: Int, before: Item, send: Boolean) {
        super.onSlotChange(index, before, send)
        if (index == 0) {
            if (saddle!!.isNull) {
                holder.setDataFlag(EntityFlag.SADDLED, false)
                holder.setDataFlag(EntityFlag.WASD_CONTROLLED, false)
                holder.setDataFlag(EntityFlag.CAN_POWER_JUMP, false)
            } else {
                holder!!.level.addLevelSoundEvent(
                    holder.position, LevelSoundEventPacket.SOUND_SADDLE, -1,
                    holder.getIdentifier(), false, false
                )
                holder.setDataFlag(EntityFlag.SADDLED)
                holder.setDataFlag(EntityFlag.WASD_CONTROLLED)
                holder.setDataFlag(EntityFlag.CAN_POWER_JUMP)
            }
        } else if (index == 1) {
            if (!horseArmor!!.isNull) {
                holder!!.level.addSound(holder.position, Sound.MOB_HORSE_ARMOR)
            }
            val mobArmorEquipmentPacket = MobArmorEquipmentPacket()
            mobArmorEquipmentPacket.eid = holder.getId()
            mobArmorEquipmentPacket.slots = arrayOf(
                Item.AIR,
                horseArmor, Item.AIR, Item.AIR
            )
            Server.broadcastPacket(this.getViewers(), mobArmorEquipmentPacket)
        }
    }

    override fun onClose(who: Player) {
        val pk = ContainerClosePacket()
        pk.windowId = who.getWindowId(this)
        pk.wasServerInitiated = who.closingWindowId != pk.windowId
        pk.type = getType()
        who.dataPacket(pk)
        super.onClose(who)
    }

    override fun onOpen(who: Player) {
        super.onOpen(who)
        who.dataPacket(createUpdateEquipmentPacket(who))
        sendContents(this.getViewers())
    }

    override var holder: InventoryHolder?
        get() = holder as EntityHorse
        set(holder) {
            super.holder = holder
        }

    protected fun createUpdateEquipmentPacket(who: Player): UpdateEquipmentPacket {
        val slots = ListTag<CompoundTag>()
        val saddle = saddle!!
        val horseArmor = horseArmor!!
        if (!saddle.isNull) {
            slots.add(
                slot0.copy().putCompound(
                    "item",
                    CompoundTag().putString("Name", saddle.id).putShort("Aux", Short.MAX_VALUE.toInt())
                )
            )
        } else slots.add(slot0.copy())
        if (!horseArmor.isNull) {
            slots.add(
                slot1.copy().putCompound(
                    "item",
                    CompoundTag().putString("Name", horseArmor.id).putShort("Aux", Short.MAX_VALUE.toInt())
                )
            )
        } else slots.add(slot1.copy())
        val nbt = CompoundTag().putList("slots", slots)
        val updateEquipmentPacket = UpdateEquipmentPacket()
        updateEquipmentPacket.windowId = who.getWindowId(this)
        updateEquipmentPacket.windowType = getType().networkType
        updateEquipmentPacket.eid = holder.getId()
        try {
            updateEquipmentPacket.namedtag = NBTIO.writeNetwork(nbt)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        return updateEquipmentPacket
    }

    companion object {
        private val slot0: CompoundTag
        private val slot1: CompoundTag

        init {
            val saddle = ListTag<CompoundTag>().add(
                CompoundTag().putCompound(
                    "slotItem", CompoundTag()
                        .putShort("Aux", Short.MAX_VALUE.toInt())
                        .putString("Name", ItemID.SADDLE)
                )
            )
            val horseArmor = ListTag<CompoundTag>()
            for (h in List.of<String>(
                ItemID.LEATHER_HORSE_ARMOR,
                ItemID.IRON_HORSE_ARMOR,
                ItemID.GOLDEN_HORSE_ARMOR,
                ItemID.DIAMOND_HORSE_ARMOR
            )) {
                horseArmor.add(
                    CompoundTag().putCompound(
                        "slotItem", CompoundTag().putShort("Aux", Short.MAX_VALUE.toInt()).putString(
                            "Name",
                            h!!
                        )
                    )
                )
            }
            slot0 = CompoundTag().putList("acceptedItems", saddle).putInt("slotNumber", 0)
            slot1 = CompoundTag().putList("acceptedItems", horseArmor).putInt("slotNumber", 1)
        }
    }
}
