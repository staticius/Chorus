package org.chorus.event.block

import org.chorus.Player
import org.chorus.block.Block
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

/**
 * @author MagicDroidX (Nukkit Project)
 */
class SignChangeEvent(block: Block, val player: Player, lines: Array<String?>) :
    BlockEvent(block), Cancellable {
    var lines: Array<String?> = arrayOfNulls(4)
        private set

    init {
        this.lines = lines
    }

    fun getLine(index: Int): String? {
        return lines[index]
    }

    fun setLine(index: Int, line: String?) {
        lines[index] = line
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
