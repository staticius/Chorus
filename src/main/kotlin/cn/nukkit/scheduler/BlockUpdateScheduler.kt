package cn.nukkit.scheduler

import cn.nukkit.block.Block
import cn.nukkit.level.Level
import cn.nukkit.math.AxisAlignedBB
import cn.nukkit.math.NukkitMath.floorDouble
import cn.nukkit.math.Vector3
import cn.nukkit.utils.BlockUpdateEntry
import cn.nukkit.utils.collection.nb.Long2ObjectNonBlockingMap
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.max

class BlockUpdateScheduler(level: Level, currentTick: Long) {
    private val level: Level
    private var lastTick: Long
    private val queuedUpdates =
        Long2ObjectNonBlockingMap<MutableSet<BlockUpdateEntry>>() // Change to ConcurrentHashMap if this needs to be concurrent

    private var pendingUpdates: MutableSet<BlockUpdateEntry>? = null

    init {
        lastTick = currentTick
        this.level = level
    }

    fun tick(currentTick: Long) {
        // Should only perform once, unless ticks were skipped
        if (currentTick - lastTick < Short.MAX_VALUE) { // Arbitrary
            for (tick in lastTick + 1..currentTick) {
                perform(tick)
            }
        } else {
            val times = ArrayList(queuedUpdates.keys)
            Collections.sort(times)
            for (tick in times) {
                if (tick <= currentTick) {
                    perform(tick)
                } else {
                    break
                }
            }
        }
        lastTick = currentTick
    }

    private fun perform(tick: Long) {
        try {
            lastTick = tick
            pendingUpdates = queuedUpdates.remove(tick)
            val updates = pendingUpdates
            if (updates != null) {
                val updateIterator = updates.iterator()

                while (updateIterator.hasNext()) {
                    val entry = updateIterator.next()

                    val pos = entry.pos
                    if (level.isChunkLoaded(floorDouble(pos.south) shr 4, floorDouble(pos.west) shr 4)) {
                        val block = level.getBlock(entry.pos, entry.block.layer)

                        updateIterator.remove()
                        if (Block.equals(block, entry.block, false) && entry.checkBlockWhenUpdate) {
                            block!!.onUpdate(Level.BLOCK_UPDATE_SCHEDULED)
                        } else {
                            block!!.onUpdate(Level.BLOCK_UPDATE_SCHEDULED)
                        }
                    } else {
                        level.scheduleUpdate(entry.block, entry.pos, 0)
                    }
                }
            }
        } finally {
            pendingUpdates = null
        }
    }

    fun getPendingBlockUpdates(boundingBox: AxisAlignedBB): Set<BlockUpdateEntry> {
        val set: MutableSet<BlockUpdateEntry> = HashSet()

        for (tickSet in queuedUpdates.values) {
            for (update in tickSet) {
                val pos = update.pos

                if (pos.getX() >= boundingBox.minX && pos.getX() < boundingBox.maxX && pos.getZ() >= boundingBox.minZ && pos.getZ() < boundingBox.maxZ) {
                    set.add(update)
                }
            }
        }

        return set
    }

    fun isBlockTickPending(pos: Vector3?, block: Block?): Boolean {
        val tmpUpdates: Set<BlockUpdateEntry>? = pendingUpdates
        if (tmpUpdates == null || tmpUpdates.isEmpty()) return false
        return tmpUpdates.contains(BlockUpdateEntry(pos, block))
    }

    private fun getMinTime(entry: BlockUpdateEntry): Long {
        return max(entry.delay.toDouble(), (lastTick + 1).toDouble()).toLong()
    }

    fun add(entry: BlockUpdateEntry) {
        val time = getMinTime(entry)
        var updateSet = queuedUpdates[time]
        if (updateSet == null) {
            val tmp =
                queuedUpdates.putIfAbsent(time, ConcurrentHashMap.newKeySet<BlockUpdateEntry>().also { updateSet = it })
            if (tmp != null) updateSet = tmp
        }
        updateSet!!.add(entry)
    }

    fun contains(entry: BlockUpdateEntry): Boolean {
        for (tickUpdateSet in queuedUpdates.values) {
            if (tickUpdateSet.contains(entry)) {
                return true
            }
        }
        return false
    }

    fun remove(entry: BlockUpdateEntry): Boolean {
        for (tickUpdateSet in queuedUpdates.values) {
            if (tickUpdateSet.remove(entry)) {
                return true
            }
        }
        return false
    }
}
