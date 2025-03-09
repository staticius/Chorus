package org.chorus.level

import cn.nukkit.level.format.IChunk

/**
 * @author MagicDroidX (Nukkit Project)
 */
interface ChunkLoader {
    val loaderId: Int

    val isLoaderActive: Boolean

    val locator: Locator?

    val x: Double

    val z: Double

    val level: Level?

    fun onChunkChanged(chunk: IChunk?)

    fun onChunkLoaded(chunk: IChunk?)

    fun onChunkUnloaded(chunk: IChunk?)

    companion object {
        val EMPTY_ARRAY: Array<ChunkLoader?> = arrayOfNulls(0)
    }
}
