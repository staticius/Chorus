package cn.nukkit.event.block

import cn.nukkit.block.Block
import cn.nukkit.block.property.enums.BigDripleafTilt
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

class BigDripleafTiltChangeEvent(block: Block, val oldTilt: BigDripleafTilt, var newTilt: BigDripleafTilt) :
    BlockEvent(block), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
