package cn.nukkit.event.block

import cn.nukkit.block.Block
import cn.nukkit.entity.Entity
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

class FarmLandDecayEvent(val entity: Entity?, farm: Block) : BlockEvent(farm), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
