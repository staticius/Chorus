package cn.nukkit.event.block

import cn.nukkit.block.Block
import cn.nukkit.block.BlockAir
import cn.nukkit.block.BlockTurtleEgg
import cn.nukkit.block.property.enums.TurtleEggCount
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

class TurtleEggHatchEvent(turtleEgg: BlockTurtleEgg, @JvmField var eggsHatching: Int, @JvmField var newState: Block) :
    BlockEvent(turtleEgg), Cancellable {
    var isRecalculateOnFailure: Boolean = true

    fun recalculateNewState() {
        var turtleEgg = getBlock()
        val eggCount = turtleEgg!!.eggCount.ordinal + 1
        val eggsHatching = this.eggsHatching
        if (eggCount <= eggsHatching) {
            newState = BlockAir()
        } else {
            turtleEgg = turtleEgg.clone()
            turtleEgg.eggCount = TurtleEggCount.entries[eggCount - eggsHatching - 1]
            newState = turtleEgg
        }
    }

    override fun getBlock(): BlockTurtleEgg? {
        return super.getBlock() as BlockTurtleEgg
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
