package cn.nukkit.level.tickingarea.manager

import cn.nukkit.level.*

abstract class TickingAreaManager(storage: TickingAreaStorage) {
    protected var storage: TickingAreaStorage = storage

    abstract fun addTickingArea(area: TickingArea)

    abstract fun removeTickingArea(name: String)

    abstract fun removeAllTickingArea()

    abstract fun getTickingArea(name: String): TickingArea?

    abstract fun containTickingArea(name: String): Boolean

    abstract val allTickingArea: Set<Any?>

    abstract fun getTickingAreaByChunk(levelName: String, chunkPos: ChunkPos): TickingArea?

    abstract fun getTickingAreaByPos(pos: Locator): TickingArea?

    abstract fun loadAllTickingArea()

    fun getStorage(): TickingAreaStorage {
        return storage
    }
}
