package org.chorus.level.util

import org.chorus.block.Block

/**
 * 实现此接口的区块应该拥有一个能够并行访问的方块缓存，通常每tick都会调用clear。
 */
interface TickCachedBlockStore {
    fun clearCachedStore()

    fun saveIntoCachedStore(block: Block, x: Int, y: Int, z: Int, layer: Int)

    fun getFromCachedStore(x: Int, y: Int, z: Int, layer: Int): Block?

    /**
     * 同computeIfAbsent
     */
    fun computeFromCachedStore(x: Int, y: Int, z: Int, layer: Int, cachedBlockComputer: CachedBlockComputer): Block?

    interface CachedBlockComputer {
        fun compute(): Block?
    }
}
