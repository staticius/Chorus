package org.chorus.inventory

import org.chorus.Player
import org.chorus.entity.mob.villagers.EntityVillagerV2
import org.chorus.network.protocol.types.itemstack.ContainerSlotType

class TradeInventory(holder: EntityVillagerV2?) : BaseInventory(holder, InventoryType.TRADE, 3) {
    override var holder: EntityVillagerV2
    var displayName: String? = null

    init {
        this.holder = holder!!
    }

    override fun init() {
        val networkedSlotMap = networkSlotMap()
        networkedSlotMap[0] = 4
        networkedSlotMap[1] = 5
        val slotTypeMap = slotTypeMap()
        slotTypeMap[0] = ContainerSlotType.TRADE2_INGREDIENT_1
        slotTypeMap[1] = ContainerSlotType.TRADE2_INGREDIENT_2
    }

    override fun getHolder(): EntityVillagerV2 {
        return this.holder
    }

    override fun onOpen(who: Player) {
        super.onOpen(who)
        val villager = this.getHolder()
        villager.setTradingPlayer(who.getId())
        villager.updateTrades(who)
    }

    override fun onClose(who: Player) {
        getHolder().setTradingPlayer(0L)
        super.onClose(who)
    }
}
