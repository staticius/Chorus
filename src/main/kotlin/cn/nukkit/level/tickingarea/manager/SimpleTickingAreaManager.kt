package cn.nukkit.level.tickingarea.manager

import cn.nukkit.entity.EntityHuman.getName
import cn.nukkit.level.*
import java.util.function.Predicate
import kotlin.collections.HashSet
import kotlin.collections.MutableMap
import kotlin.collections.Set
import kotlin.collections.set

class SimpleTickingAreaManager(storage: TickingAreaStorage) : TickingAreaManager(storage) {
    protected var areaMap: MutableMap<String, TickingArea> = storage.readTickingArea()

    override fun addTickingArea(area: TickingArea) {
        areaMap[area.getName()] = area
        storage.addTickingArea(area)
    }

    override fun removeTickingArea(name: String) {
        areaMap.remove(name)
        storage.removeTickingArea(name)
    }

    override fun removeAllTickingArea() {
        areaMap.clear()
        storage.removeAllTickingArea()
    }

    override fun getTickingArea(name: String): TickingArea? {
        return areaMap[name]
    }

    override fun containTickingArea(name: String): Boolean {
        return areaMap.containsKey(name)
    }

    override val allTickingArea: Set<Any?>
        get() = HashSet<TickingArea>(areaMap.values())

    override fun getTickingAreaByChunk(levelName: String, chunkPos: ChunkPos): TickingArea? {
        var matchedArea: TickingArea? = null
        for (area in areaMap.values()) {
            val matched = area.getLevelName() == levelName && area.getChunks().stream()
                .anyMatch(Predicate<ChunkPos> { pos: ChunkPos -> pos == chunkPos })
            if (matched) {
                matchedArea = area
                break
            }
        }
        return matchedArea
    }

    override fun getTickingAreaByPos(pos: Locator): TickingArea? {
        return getTickingAreaByChunk(pos.levelName, ChunkPos(pos.position.chunkX, pos.position.chunkZ))
    }

    override fun loadAllTickingArea() {
        for (area in areaMap.values()) if (!area.loadAllChunk()) removeTickingArea(area.getName())
    }
}
