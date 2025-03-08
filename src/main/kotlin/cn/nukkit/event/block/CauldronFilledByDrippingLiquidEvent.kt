package cn.nukkit.event.block

import cn.nukkit.block.Block
import cn.nukkit.block.property.enums.CauldronLiquid
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

class CauldronFilledByDrippingLiquidEvent(cauldron: Block, @JvmField var liquid: CauldronLiquid, @JvmField var liquidLevelIncrement: Int) :
    BlockEvent(cauldron), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
