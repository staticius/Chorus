package org.chorus.level.format

import io.netty.buffer.ByteBuf
import org.chorus.block.Block
import org.chorus.block.BlockAir
import org.chorus.block.BlockState
import org.chorus.level.Level
import org.chorus.level.biome.BiomeID
import org.chorus.level.format.bitarray.BitArrayVersion
import org.chorus.level.format.palette.BlockPalette
import org.chorus.level.format.palette.Palette
import org.chorus.math.BlockVector3
import org.chorus.registry.Registries
import java.util.concurrent.atomic.AtomicLong
import java.util.function.BiPredicate
import javax.annotation.concurrent.NotThreadSafe
import kotlin.math.max
import kotlin.math.min

@NotThreadSafe
@JvmRecord
data class ChunkSection(
    val y: Byte,
    val blockLayer: Array<BlockPalette>,
    val biomes: Palette<Int>,
    val blockLights: ByteArray,
    val skyLights: ByteArray,
    val blockChanges: AtomicLong
) {
    constructor(sectionY: Byte) : this(
        sectionY,
        arrayOf<BlockPalette>(
            BlockPalette(
                BlockAir.properties.defaultState,
                MutableList<BlockState>(16) { BlockAir.STATE },
                BitArrayVersion.V2
            ),
            BlockPalette(
                BlockAir.properties.defaultState,
                MutableList<BlockState>(16) { BlockAir.STATE },
                BitArrayVersion.V2
            )
        ),
        Palette<Int>(BiomeID.PLAINS),
        ByteArray(SIZE),
        ByteArray(SIZE),
        AtomicLong(0)
    )

    constructor(sectionY: Byte, blockLayer: Array<BlockPalette>) : this(
        sectionY, blockLayer,
        Palette<Int>(BiomeID.PLAINS),
        ByteArray(SIZE),
        ByteArray(SIZE),
        AtomicLong(0)
    )

    fun getBlockState(x: Int, y: Int, z: Int): BlockState {
        return getBlockState(x, y, z, 0)
    }

    fun getBlockState(x: Int, y: Int, z: Int, layer: Int): BlockState {
        return blockLayer[layer][IChunk.index(x, y, z)]
    }

    fun setBlockState(x: Int, y: Int, z: Int, blockState: BlockState, layer: Int) {
        blockChanges.addAndGet(1)
        blockLayer[layer][IChunk.index(x, y, z)] = blockState
    }

    fun getAndSetBlockState(x: Int, y: Int, z: Int, blockState: BlockState, layer: Int): BlockState {
        blockChanges.addAndGet(1)
        val result = blockLayer[layer][IChunk.index(x, y, z)]
        blockLayer[layer][IChunk.index(x, y, z)] = blockState
        return result
    }

    fun setBiomeId(x: Int, y: Int, z: Int, biomeId: Int) {
        biomes[IChunk.index(x, y, z)] = biomeId
    }

    fun getBiomeId(x: Int, y: Int, z: Int): Int {
        return biomes[IChunk.index(x, y, z)]
    }

    fun getBlockLight(x: Int, y: Int, z: Int): Byte {
        return blockLights[IChunk.index(x, y, z)]
    }

    fun getBlockSkyLight(x: Int, y: Int, z: Int): Byte {
        return skyLights[IChunk.index(x, y, z)]
    }

    fun setBlockLight(x: Int, y: Int, z: Int, light: Byte) {
        blockLights[IChunk.index(x, y, z)] = light
    }

    fun setBlockSkyLight(x: Int, y: Int, z: Int, light: Byte) {
        skyLights[IChunk.index(x, y, z)] = light
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
        for (x in min((max.x - offsetX), 15) downTo minX) {
            current.x = offsetX + x
            for (z in min((max.z - offsetZ), 15) downTo minZ) {
                current.z = offsetZ + z
                for (y in min((max.y - offsetY), 15) downTo minY) {
                    current.y = offsetY + y
                    val state = blockLayer[0][IChunk.index(x, y, z)]
                    if (condition.test(current, state)) {
                        results.add(Registries.BLOCK[state, current.x, current.y, current.z, provider.level]!!)
                    }
                }
            }
        }
        return results
    }

    val isEmpty: Boolean
        get() = blockLayer[0].isEmpty && blockLayer[0][0] === BlockAir.properties.defaultState

    fun setNeedReObfuscate() {
        blockLayer[0].setNeedReObfuscate()
        blockLayer[1].setNeedReObfuscate()
    }

    fun writeToBuf(byteBuf: ByteBuf) {
        byteBuf.writeByte(VERSION)
        //block layer count
        byteBuf.writeByte(LAYER_COUNT)
        byteBuf.writeByte(y.toInt())

        blockLayer[0].writeToNetwork(byteBuf) { it.blockStateHash() }
        blockLayer[1].writeToNetwork(byteBuf) { it.blockStateHash() }
    }

    fun writeObfuscatedToBuf(level: Level, byteBuf: ByteBuf) {
        byteBuf.writeByte(VERSION)
        //block layer count
        byteBuf.writeByte(LAYER_COUNT)
        byteBuf.writeByte(y.toInt())

        blockLayer[0].writeObfuscatedToNetwork(
            level, blockChanges, byteBuf
        ) { it.blockStateHash() }
        blockLayer[1].writeObfuscatedToNetwork(
            level, blockChanges, byteBuf
        ) { it.blockStateHash() }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChunkSection

        if (y != other.y) return false
        if (!blockLayer.contentEquals(other.blockLayer)) return false
        if (biomes != other.biomes) return false
        if (!blockLights.contentEquals(other.blockLights)) return false
        if (!skyLights.contentEquals(other.skyLights)) return false
        if (blockChanges != other.blockChanges) return false

        return true
    }

    override fun hashCode(): Int {
        var result: Int = y.toInt()
        result = 31 * result + blockLayer.contentHashCode()
        result = 31 * result + biomes.hashCode()
        result = 31 * result + blockLights.contentHashCode()
        result = 31 * result + skyLights.contentHashCode()
        result = 31 * result + blockChanges.hashCode()
        return result
    }

    companion object {
        const val SIZE: Int = 16 * 16 * 16
        const val LAYER_COUNT: Int = 2
        const val VERSION: Int = 9
    }
}
