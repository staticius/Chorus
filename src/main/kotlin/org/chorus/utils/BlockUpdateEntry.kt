package org.chorus.utils

import org.chorus.block.Block
import org.chorus.math.Vector3
import java.util.concurrent.atomic.AtomicLong


class BlockUpdateEntry : Comparable<BlockUpdateEntry> {
    var priority: Int = 0
    var delay: Long = 0


    var checkBlockWhenUpdate: Boolean = true

    val pos: Vector3
    val block: Block

    val id: Long

    constructor(pos: Vector3, block: Block) {
        this.pos = pos
        this.block = block
        this.id = entryID.getAndIncrement()
    }

    constructor(pos: Vector3, block: Block, delay: Long, priority: Int) {
        this.id = entryID.getAndIncrement()
        this.pos = pos
        this.priority = priority
        this.delay = delay
        this.block = block
    }

    constructor(pos: Vector3, block: Block, delay: Long, priority: Int, checkBlockWhenUpdate: Boolean) {
        this.id = entryID.getAndIncrement()
        this.pos = pos
        this.priority = priority
        this.delay = delay
        this.block = block
        this.checkBlockWhenUpdate = checkBlockWhenUpdate
    }

    override fun compareTo(entry: BlockUpdateEntry): Int {
        return if (this.delay < entry.delay) -1 else (if (this.delay > entry.delay) 1 else (if (this.priority != entry.priority) this.priority - entry.priority else java.lang.Long.compare(
            this.id, entry.id
        )))
    }

    override fun equals(`object`: Any?): Boolean {
        if (`object` !is BlockUpdateEntry) {
            if (`object` is Block) {
                return `object`.layer == block.layer && pos.equals(`object`)
            }
            if (`object` is Vector3) {
                return block.layer == 0 && pos.equals(`object`)
            }
            return false
        } else {
            return block.layer == `object`.block.layer && pos.equals(`object`.pos) && Block.equals(
                this.block,
                `object`.block,
                false
            )
        }
    }

    override fun hashCode(): Int {
        return pos.hashCode()
    }

    companion object {
        private val entryID = AtomicLong(0)
    }
}
