package org.chorus.level.format

import cn.nukkit.level.DimensionData
import cn.nukkit.level.GameRules
import cn.nukkit.level.Level
import cn.nukkit.math.Vector3
import it.unimi.dsi.fastutil.Pair

/**
 * @author MagicDroidX (Nukkit Project)
 */
interface LevelProvider {
    val dimensionData: DimensionData

    fun requestChunkData(x: Int, z: Int): Pair<ByteArray, Int>

    val path: String

    fun getLoadedChunk(x: Int, z: Int): IChunk?

    fun getLoadedChunk(hash: Long): IChunk?

    fun getChunk(x: Int, z: Int): IChunk?

    fun getChunk(x: Int, z: Int, create: Boolean): IChunk?

    fun getEmptyChunk(x: Int, z: Int): IChunk

    fun saveChunks()

    fun saveChunk(x: Int, z: Int)

    fun saveChunk(x: Int, z: Int, chunk: IChunk)

    fun unloadChunks()

    fun loadChunk(x: Int, z: Int): Boolean

    fun loadChunk(x: Int, z: Int, create: Boolean): Boolean

    fun unloadChunk(x: Int, z: Int): Boolean

    fun unloadChunk(x: Int, z: Int, safe: Boolean): Boolean

    fun isChunkGenerated(x: Int, z: Int): Boolean

    fun isChunkPopulated(x: Int, z: Int): Boolean

    fun isChunkLoaded(x: Int, z: Int): Boolean

    fun isChunkLoaded(hash: Long): Boolean

    fun setChunk(chunkX: Int, chunkZ: Int, chunk: IChunk)

    val name: String?

    var isRaining: Boolean

    var rainTime: Int

    var isThundering: Boolean

    var thunderTime: Int

    var currentTick: Long

    var time: Long

    var seed: Long

    var spawn: Vector3?

    val loadedChunks: Map<Long, IChunk>

    @JvmField
    val level: Level?

    fun close()

    fun saveLevelData()

    fun updateLevelName(name: String)

    val gamerules: GameRules?

    fun setGameRules(rules: GameRules?)

    val maximumLayer: Int
        get() = 1 //two layer 0,1

    val isOverWorld: Boolean
        get() = dimensionData.dimensionId == 0

    val isNether: Boolean
        get() = dimensionData.dimensionId == 1

    val isTheEnd: Boolean
        get() = dimensionData.dimensionId == 2

    companion object {
        const val ORDER_YZX: Byte = 0
        const val ORDER_ZXY: Byte = 1
    }
}
