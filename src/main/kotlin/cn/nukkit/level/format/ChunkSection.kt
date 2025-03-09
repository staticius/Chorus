package cn.nukkit.level.format

import cn.nukkit.block.*
import cn.nukkit.level.*
import cn.nukkit.level.biome.BiomeID
import cn.nukkit.level.format.bitarray.BitArrayVersion
import cn.nukkit.level.format.palette.*
import cn.nukkit.level.util.NibbleArray
import cn.nukkit.math.BlockVector3
import cn.nukkit.registry.Registries
import io.netty.buffer.ByteBuf
import it.unimi.dsi.fastutil.objects.ReferenceArrayList
import java.util.concurrent.atomic.AtomicLong
import java.util.function.BiPredicate
import javax.annotation.concurrent.NotThreadSafe
import kotlin.math.max
import kotlin.math.min

/**
 * Allay Project 2023/5/30
 *
 * @author Cool_Loong
 */
@NotThreadSafe
@JvmRecord
data class ChunkSection(
    val y: Byte,
    val blockLayer: Array<BlockPalette?>,
    val biomes: Palette<Int?>,
    val blockLights: NibbleArray,
    val skyLights: NibbleArray,
    val blockChanges: AtomicLong
) {
    constructor(sectionY: Byte) : this(
        sectionY,
        arrayOf<BlockPalette?>(
            BlockPalette(BlockAir.properties.defaultState, ReferenceArrayList<BlockState?>(16), BitArrayVersion.V2),
            BlockPalette(BlockAir.properties.defaultState, ReferenceArrayList<BlockState?>(16), BitArrayVersion.V2)
        ),
        Palette<Int?>(BiomeID.Companion.PLAINS),
        NibbleArray(SIZE),
        NibbleArray(SIZE),
        AtomicLong(0)
    )

    constructor(sectionY: Byte, blockLayer: Array<BlockPalette?>) : this(
        sectionY, blockLayer,
        Palette<Int?>(BiomeID.Companion.PLAINS),
        NibbleArray(SIZE),
        NibbleArray(SIZE),
        AtomicLong(0)
    )

    fun getBlockState(x: Int, y: Int, z: Int): BlockState? {
        return getBlockState(x, y, z, 0)
    }

    fun getBlockState(x: Int, y: Int, z: Int, layer: Int): BlockState? {
        return blockLayer[layer]!![IChunk.Companion.index(x, y, z)]
    }

    fun setBlockState(x: Int, y: Int, z: Int, blockState: BlockState?, layer: Int) {
        blockChanges.addAndGet(1)
        blockLayer[layer]!![IChunk.Companion.index(x, y, z)] = blockState
    }

    fun getAndSetBlockState(x: Int, y: Int, z: Int, blockstate: BlockState?, layer: Int): BlockState? {
        blockChanges.addAndGet(1)
        val result = blockLayer[layer]!![IChunk.Companion.index(x, y, z)]
        blockLayer[layer]!![IChunk.Companion.index(x, y, z)] = blockstate
        return result
    }

    fun setBiomeId(x: Int, y: Int, z: Int, biomeId: Int) {
        biomes[IChunk.Companion.index(x, y, z)] = biomeId
    }

    fun getBiomeId(x: Int, y: Int, z: Int): Int {
        return biomes[IChunk.Companion.index(x, y, z)]!!
    }

    fun getBlockLight(x: Int, y: Int, z: Int): Byte {
        return blockLights[IChunk.Companion.index(x, y, z)]
    }

    fun getBlockSkyLight(x: Int, y: Int, z: Int): Byte {
        return skyLights[IChunk.Companion.index(x, y, z)]
    }

    fun setBlockLight(x: Int, y: Int, z: Int, light: Byte) {
        blockLights[IChunk.Companion.index(x, y, z)] = light
    }

    fun setBlockSkyLight(x: Int, y: Int, z: Int, light: Byte) {
        skyLights[IChunk.Companion.index(x, y, z)] = light
    }

    fun scanBlocks(
        provider: LevelProvider,
        offsetX: Int,
        offsetZ: Int,
        min: BlockVector3,
        max: BlockVector3,
        condition: BiPredicate<BlockVector3?, BlockState?>
    ): List<Block> {
        val results: MutableList<Block> = ArrayList()
        val current = BlockVector3()
        val offsetY = y.toInt() shl 4
        val minX = max(0.0, (min.x - offsetX).toDouble()).toInt()
        val minY = max(0.0, (min.y - offsetY).toDouble()).toInt()
        val minZ = max(0.0, (min.z - offsetZ).toDouble()).toInt()
        for (x in min((max.x - offsetX).toDouble(), 15.0) downTo minX) {
            current.x = offsetX + x
            for (z in min((max.z - offsetZ).toDouble(), 15.0) downTo minZ) {
                current.z = offsetZ + z
                for (y in min((max.y - offsetY).toDouble(), 15.0) downTo minY) {
                    current.y = offsetY + y
                    val state = blockLayer[0]!![IChunk.Companion.index(x, y, z)]
                    if (condition.test(current, state)) {
                        results.add(Registries.BLOCK[state, current.x, current.y, current.z, provider.level])
                    }
                }
            }
        }
        return results
    }

    val isEmpty: Boolean
        get() = blockLayer[0].isEmpty() && blockLayer[0]!![0] === BlockAir.properties.defaultState

    fun setNeedReObfuscate() {
        blockLayer[0]!!.setNeedReObfuscate()
        blockLayer[1]!!.setNeedReObfuscate()
    }

    fun writeToBuf(byteBuf: ByteBuf) {
        byteBuf.writeByte(VERSION)
        //block layer count
        byteBuf.writeByte(LAYER_COUNT)
        byteBuf.writeByte(y.toInt())

        blockLayer[0]!!.writeToNetwork(byteBuf) { obj: V? -> obj.blockStateHash() }
        blockLayer[1]!!.writeToNetwork(byteBuf) { obj: V? -> obj.blockStateHash() }
    }

    fun writeObfuscatedToBuf(level: Level, byteBuf: ByteBuf) {
        byteBuf.writeByte(VERSION)
        //block layer count
        byteBuf.writeByte(LAYER_COUNT)
        byteBuf.writeByte(y.toInt())

        blockLayer[0]!!.writeObfuscatedToNetwork(
            level, blockChanges, byteBuf
        ) { obj: V? -> obj.blockStateHash() }
        blockLayer[1]!!.writeObfuscatedToNetwork(
            level, blockChanges, byteBuf
        ) { obj: V? -> obj.blockStateHash() }
    }

    companion object {
        const val SIZE: Int = 16 * 16 * 16
        const val LAYER_COUNT: Int = 2
        const val VERSION: Int = 9
    }
}
