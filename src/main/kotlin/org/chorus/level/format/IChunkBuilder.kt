package org.chorus.level.format

import org.chorus.level.DimensionData
import org.chorus.nbt.tag.CompoundTag

/**
 * Allay Project 12/16/2023
 *
 * @author Cool_Loong
 */
interface IChunkBuilder {
    fun chunkX(chunkX: Int): IChunkBuilder

    val chunkX: Int

    fun chunkZ(chunkZ: Int): IChunkBuilder

    val chunkZ: Int

    fun state(state: ChunkState?): IChunkBuilder

    fun levelProvider(levelProvider: LevelProvider?): IChunkBuilder

    val dimensionData: DimensionData?

    fun sections(sections: Array<ChunkSection?>?): IChunkBuilder

    val sections: Array<ChunkSection?>?

    fun heightMap(heightMap: ShortArray?): IChunkBuilder

    fun entities(entities: List<CompoundTag>?): IChunkBuilder

    fun blockEntities(blockEntities: List<CompoundTag>?): IChunkBuilder

    fun extraData(extraData: CompoundTag?): IChunkBuilder

    fun build(): IChunk

    fun emptyChunk(chunkX: Int, chunkZ: Int): IChunk
}
