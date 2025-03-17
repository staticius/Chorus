package org.chorus.level

import org.chorus.level.format.IChunk

interface ChunkLoader {
    val loaderId: Int

    val isLoaderActive: Boolean

    val level: Level?

    fun getLocator(): Locator

    fun onChunkChanged(chunk: IChunk)

    fun onChunkLoaded(chunk: IChunk)

    fun onChunkUnloaded(chunk: IChunk)

    companion object {
        val EMPTY_ARRAY: Array<ChunkLoader> = emptyArray()
    }
}
