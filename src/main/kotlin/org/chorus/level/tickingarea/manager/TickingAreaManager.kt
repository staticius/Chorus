package org.chorus.level.tickingarea.manager

import org.chorus.level.Locator
import org.chorus.level.tickingarea.TickingArea
import org.chorus.level.tickingarea.storage.TickingAreaStorage

abstract class TickingAreaManager(protected var storage: TickingAreaStorage) {

    abstract fun addTickingArea(area: TickingArea)

    abstract fun removeTickingArea(name: String)

    abstract fun removeAllTickingArea()

    abstract fun getTickingArea(name: String): TickingArea?

    abstract fun containTickingArea(name: String): Boolean

    abstract val allTickingArea: Set<TickingArea>

    abstract fun getTickingAreaByChunk(levelName: String, chunkPos: TickingArea.ChunkPos): TickingArea?

    abstract fun getTickingAreaByPos(pos: Locator): TickingArea?

    abstract fun loadAllTickingArea()

    fun getStorage(): TickingAreaStorage {
        return storage
    }
}
