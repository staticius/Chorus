package org.chorus.inventory

import cn.nukkit.AdventureSettings
import cn.nukkit.Player
import cn.nukkit.level.vibration.VibrationEvent
import cn.nukkit.level.vibration.VibrationType
import cn.nukkit.math.NukkitMath
import cn.nukkit.math.Vector3
import cn.nukkit.network.protocol.ContainerOpenPacket
import kotlin.math.min

abstract class ContainerInventory(holder: InventoryHolder?, type: InventoryType, size: Int) :
    BaseInventory(holder, type, size) {
    override var holder: InventoryHolder?
        get() = super.getHolder()
        set(holder) {
            super.holder = holder
        }

    override fun onOpen(who: Player) {
        if (!who.adventureSettings[AdventureSettings.Type.OPEN_CONTAINERS]) return
        super.onOpen(who)
        val pk = ContainerOpenPacket()
        pk.windowId = who.getWindowId(this)
        pk.type = getType().networkType
        val holder = this.holder
        pk.x = holder.x.toInt()
        pk.y = holder.y.toInt()
        pk.z = holder.z.toInt()
        who.dataPacket(pk)

        this.sendContents(who)

        if (canCauseVibration() && holder is Vector3) {
            who.level!!.vibrationManager.callVibrationEvent(
                VibrationEvent(
                    who,
                    holder.add(0.5, 0.5, 0.5),
                    VibrationType.CONTAINER_OPEN
                )
            )
        }
    }

    override fun onClose(who: Player) {
        if (canCauseVibration() && holder is Vector3) {
            who.level!!.vibrationManager.callVibrationEvent(
                VibrationEvent(
                    who,
                    vector3.add(0.5, 0.5, 0.5),
                    VibrationType.CONTAINER_CLOSE
                )
            )
        }
        super.onClose(who)
    }

    /**
     * 若返回为true,则在inventory打开和关闭时会发生振动事件 (InventoryHolder为Vector3子类的前提下)
     *
     * @return boolean
     */
    open fun canCauseVibration(): Boolean {
        return false
    }

    companion object {
        @JvmStatic
        fun calculateRedstone(inv: Inventory?): Int {
            if (inv == null) {
                return 0
            } else {
                var itemCount = 0
                var averageCount = 0f

                for (slot in 0..<inv.size) {
                    val item = inv.getItem(slot)

                    if (!item.isNull) {
                        averageCount += item.getCount().toFloat() / min(
                            inv.maxStackSize.toDouble(),
                            item.maxStackSize.toDouble()
                        ).toFloat()
                        ++itemCount
                    }
                }

                averageCount = averageCount / inv.size.toFloat()
                return NukkitMath.floorFloat(averageCount * 14) + (if (itemCount > 0) 1 else 0)
            }
        }
    }
}
