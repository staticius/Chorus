package cn.nukkit.level.tickingarea

import cn.nukkit.Server
import java.util.concurrent.ThreadLocalRandom

class TickingArea(name: String, levelName: String, vararg chunks: ChunkPos) {
    var name: String? = null
        protected set
    var levelName: String
        protected set
    var chunks: Set<ChunkPos> = HashSet()
        protected set

    init {
        if (!name.isEmpty()) this.name = name
        else {
            var randomName = randomName()
            val manager: TickingAreaManager = Server.getInstance().tickingAreaManager
            while (manager.containTickingArea(randomName)) randomName = randomName()
            this.name = randomName
        }
        this.levelName = levelName
        for (chunk in chunks) {
            addChunk(chunk)
        }
    }

    fun addChunk(chunk: ChunkPos) {
        chunks.add(chunk)
    }

    fun loadAllChunk(): Boolean {
        if (!Server.getInstance().loadLevel(levelName)) return false
        val level = Server.getInstance().getLevelByName(levelName)
        for (pos in chunks) {
            level.loadChunk(pos.x, pos.z)
        }
        return true
    }

    //two entry [0] => min, [1] => max
    fun minAndMaxChunkPos(): List<ChunkPos> {
        val min = ChunkPos(Integer.MAX_VALUE, Integer.MAX_VALUE)
        val max = ChunkPos(Integer.MIN_VALUE, Integer.MIN_VALUE)
        for (pos in chunks) {
            if (pos.x < min.x) min.x = pos.x
            if (pos.z < min.z) min.z = pos.z
            if (pos.x > max.x) max.x = pos.x
            if (pos.z > max.z) max.z = pos.z
        }
        return java.util.List.of(min, max)
    }

    private fun randomName(): String {
        return "Area" + ThreadLocalRandom.current().nextInt(0, java.lang.Short.MAX_VALUE - java.lang.Short.MIN_VALUE)
    }

    class ChunkPos(var x: Int, var z: Int) {
        override fun equals(obj: Any?): Boolean {
            if (obj is ChunkPos) return obj.x == this.x && obj.z == this.z
            return false
        }

        override fun hashCode(): Int {
            return x xor (z shl 12)
        }
    }
}
