package org.chorus.event.block

import org.chorus.block.Block
import org.chorus.block.BlockAir
import org.chorus.block.BlockTuff
import org.chorus.block.BlockTurtleEgg
import org.chorus.block.property.enums.TurtleEggCount
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class TurtleEggHatchEvent(turtleEgg: BlockTurtleEgg, @JvmField var eggsHatching: Int, @JvmField var newState: Block) :
    BlockEvent(turtleEgg), Cancellable {
    var isRecalculateOnFailure: Boolean = true

    fun recalculateNewState() {
        var turtleEgg = getBlock()
        val eggCount = turtleEgg.eggCount.ordinal + 1
        val eggsHatching = this.eggsHatching
        if (eggCount <= eggsHatching) {
            newState = BlockAir()
        } else {
            turtleEgg = turtleEgg.clone()
            turtleEgg.eggCount = TurtleEggCount.entries[eggCount - eggsHatching - 1]
            newState = turtleEgg
        }
    }

    override fun getBlock(): BlockTurtleEgg {
        return super.getBlock() as BlockTurtleEgg
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
