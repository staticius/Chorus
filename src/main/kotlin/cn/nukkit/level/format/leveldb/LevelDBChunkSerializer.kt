package cn.nukkit.level.format.leveldb

import cn.nukkit.Player
import cn.nukkit.block.BlockAir
import cn.nukkit.block.BlockUnknown
import cn.nukkit.blockentity.BlockEntity
import cn.nukkit.entity.Entity
import cn.nukkit.level.format.*
import cn.nukkit.level.format.bitarray.BitArrayVersion
import cn.nukkit.level.format.palette.*
import cn.nukkit.level.util.LevelDBKeyUtil
import cn.nukkit.nbt.NBTIO
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.registry.Registries
import cn.nukkit.utils.*
import com.google.common.base.Predicates
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.ByteBufOutputStream
import io.netty.buffer.Unpooled
import it.unimi.dsi.fastutil.objects.ReferenceArrayList
import org.iq80.leveldb.DB
import org.iq80.leveldb.WriteBatch
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

/**
 * Allay Project 8/23/2023
 *
 * @author Cool_Loong
 */
class LevelDBChunkSerializer private constructor() {
    fun serialize(writeBatch: WriteBatch, chunk: IChunk) {
        chunk.batchProcess { unsafeChunk: UnsafeChunk ->
            try {
                writeBatch.put(
                    LevelDBKeyUtil.VERSION.getKey(
                        unsafeChunk.x,
                        unsafeChunk.z,
                        unsafeChunk.provider.dimensionData
                    ), byteArrayOf(IChunk.Companion.VERSION.toByte())
                )
                writeBatch.put(
                    LevelDBKeyUtil.CHUNK_FINALIZED_STATE.getKey(
                        unsafeChunk.x,
                        unsafeChunk.z,
                        unsafeChunk.dimensionData
                    ), Unpooled.buffer(4).writeIntLE(unsafeChunk.chunkState.ordinal - 1).array()
                )
                serializeBlock(writeBatch, unsafeChunk)
                serializeHeightAndBiome(writeBatch, unsafeChunk)
                serializeLight(writeBatch, unsafeChunk)
                writeBatch.put(
                    LevelDBKeyUtil.PNX_EXTRA_DATA.getKey(
                        unsafeChunk.x,
                        unsafeChunk.z,
                        unsafeChunk.dimensionData
                    ), NBTIO.write(unsafeChunk.extraData)
                )
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
        //Spawning block entities requires call the getSpawnPacket method,
        //which is easy to call Level#getBlock, which can cause a deadlock,
        //so handle it without locking
        serializeTileAndEntity(writeBatch, chunk)
    }

    @Throws(IOException::class)
    fun deserialize(db: DB, builder: IChunkBuilder) {
        var versionValue = db[LevelDBKeyUtil.VERSION.getKey(builder.chunkX, builder.chunkZ, builder.dimensionData)]
        if (versionValue == null) {
            versionValue =
                db[LevelDBKeyUtil.LEGACY_VERSION.getKey(builder.chunkX, builder.chunkZ, builder.dimensionData)]
        }
        if (versionValue == null) {
            return
        }
        val finalized =
            db[LevelDBKeyUtil.CHUNK_FINALIZED_STATE.getKey(builder.chunkX, builder.chunkZ, builder.dimensionData)]
        if (finalized == null) {
            builder.state(ChunkState.FINISHED)
        } else {
            val byteBuf = Unpooled.wrappedBuffer(finalized)
            val i = if (byteBuf.readableBytes() >= 4) byteBuf.readIntLE() else byteBuf.readByte().toInt()
            builder.state(ChunkState.entries[i + 1])
        }
        val extraData = db[LevelDBKeyUtil.PNX_EXTRA_DATA.getKey(builder.chunkX, builder.chunkZ, builder.dimensionData)]
        var pnxExtraData: CompoundTag? = null
        if (extraData != null) {
            builder.extraData(NBTIO.read(extraData).also { pnxExtraData = it })
        }
        deserializeBlock(db, builder, pnxExtraData)
        deserializeHeightAndBiome(db, builder, pnxExtraData)
        deserializeTileAndEntity(db, builder, pnxExtraData)
        deserializeLight(db, builder, pnxExtraData)
    }

    //serialize chunk section light
    private fun serializeLight(writeBatch: WriteBatch, chunk: UnsafeChunk) {
        val sections = chunk.sections
        for (section in sections!!) {
            if (section == null) {
                continue
            }
            val buffer = ByteBufAllocator.DEFAULT.ioBuffer()
            try {
                val blockLights = section.blockLights.data
                buffer.writeInt(blockLights!!.size)
                buffer.writeBytes(blockLights)
                val skyLights = section.skyLights.data
                buffer.writeInt(skyLights!!.size)
                buffer.writeBytes(skyLights)
                writeBatch.put(
                    LevelDBKeyUtil.PNX_LIGHT.getKey(
                        chunk.x,
                        chunk.z,
                        section.y.toInt(),
                        chunk.provider.dimensionData
                    ), Utils.convertByteBuf2Array(buffer)
                )
            } finally {
                buffer.release()
            }
        }
    }

    private fun deserializeLight(db: DB, builder: IChunkBuilder, pnxExtraData: CompoundTag?) {
        val dimensionInfo = builder.dimensionData
        val minSectionY = dimensionInfo.minSectionY
        for (ySection in minSectionY..dimensionInfo.maxSectionY) {
            val bytes = db[LevelDBKeyUtil.PNX_LIGHT.getKey(
                builder.chunkX, builder.chunkZ, ySection,
                dimensionInfo!!
            )]
            if (bytes != null) {
                val byteBuf = ByteBufAllocator.DEFAULT.ioBuffer()
                try {
                    byteBuf.writeBytes(bytes)
                    val i = byteBuf.readInt()
                    val blockLights = ByteArray(i)
                    byteBuf.readBytes(blockLights)
                    val i2 = byteBuf.readInt()
                    val skyLights = ByteArray(i2)
                    byteBuf.readBytes(skyLights)
                    val section = builder.sections[ySection - minSectionY]
                    section!!.blockLights.copyFrom(blockLights)
                    section.skyLights.copyFrom(skyLights)
                } finally {
                    byteBuf.release()
                }
            }
        }
    }

    //serialize chunk section
    private fun serializeBlock(writeBatch: WriteBatch, chunk: UnsafeChunk) {
        val sections = chunk.sections
        for (section in sections!!) {
            if (section == null) {
                continue
            }
            val buffer = ByteBufAllocator.DEFAULT.ioBuffer()
            try {
                buffer.writeByte(ChunkSection.Companion.VERSION)
                buffer.writeByte(ChunkSection.Companion.LAYER_COUNT)
                buffer.writeByte(section.y.toInt())
                for (i in 0..<ChunkSection.Companion.LAYER_COUNT) {
                    section.blockLayer[i]!!.writeToStoragePersistent(
                        buffer
                    ) { obj: V? -> obj.getBlockStateTag() }
                }
                writeBatch.put(
                    LevelDBKeyUtil.CHUNK_SECTION_PREFIX.getKey(
                        chunk.x,
                        chunk.z,
                        section.y.toInt(),
                        chunk.provider.dimensionData
                    ), Utils.convertByteBuf2Array(buffer)
                )
            } finally {
                buffer.release()
            }
        }
    }

    //serialize chunk section
    private fun deserializeBlock(db: DB, builder: IChunkBuilder, pnxExtraData: CompoundTag?) {
        val dimensionInfo = builder.dimensionData
        val sections = arrayOfNulls<ChunkSection>(dimensionInfo.chunkSectionCount)
        val minSectionY = dimensionInfo.minSectionY
        for (ySection in minSectionY..dimensionInfo.maxSectionY) {
            val bytes = db[LevelDBKeyUtil.CHUNK_SECTION_PREFIX.getKey(
                builder.chunkX, builder.chunkZ, ySection,
                dimensionInfo!!
            )]
            if (bytes != null) {
                val byteBuf = ByteBufAllocator.DEFAULT.ioBuffer()
                try {
                    byteBuf.writeBytes(bytes)
                    val subChunkVersion = byteBuf.readByte()
                    var layers = 2
                    when (subChunkVersion) {
                        8, 9 -> {
                            layers = byteBuf.readByte().toInt() //layers
                            if (subChunkVersion.toInt() == 9) {
                                byteBuf.readByte() //sectionY not use
                            }
                            val section: ChunkSection
                            if (layers <= 2) {
                                section = ChunkSection(ySection.toByte())
                            } else {
                                val palettes = arrayOfNulls<BlockPalette>(layers)
                                Arrays.fill(
                                    palettes,
                                    BlockPalette(
                                        BlockAir.properties.defaultState,
                                        ReferenceArrayList(16),
                                        BitArrayVersion.V2
                                    )
                                )
                                section = ChunkSection(ySection.toByte(), palettes)
                            }
                            var layer = 0
                            while (layer < layers) {
                                section.blockLayer[layer]!!.readFromStoragePersistent(
                                    byteBuf
                                ) { hash: Int ->
                                    val blockState = Registries.BLOCKSTATE[hash]
                                        ?: return@readFromStoragePersistent BlockUnknown.properties.defaultState
                                    blockState
                                }
                                layer++
                            }
                            sections[ySection - minSectionY] = section
                        }

                        1 -> {
                            val section: ChunkSection
                            if (layers <= 2) {
                                section = ChunkSection(ySection.toByte())
                            } else {
                                val palettes = arrayOfNulls<BlockPalette>(layers)
                                Arrays.fill(
                                    palettes,
                                    BlockPalette(
                                        BlockAir.properties.defaultState,
                                        ReferenceArrayList(16),
                                        BitArrayVersion.V2
                                    )
                                )
                                section = ChunkSection(ySection.toByte(), palettes)
                            }
                            var layer = 0
                            while (layer < layers) {
                                section.blockLayer[layer]!!.readFromStoragePersistent(
                                    byteBuf
                                ) { hash: Int ->
                                    val blockState = Registries.BLOCKSTATE[hash]
                                        ?: return@readFromStoragePersistent BlockUnknown.properties.defaultState
                                    blockState
                                }
                                layer++
                            }
                            sections[ySection - minSectionY] = section
                        }
                    }
                } finally {
                    byteBuf.release()
                }
            }
            builder.sections(sections)
        }
    }

    //write biomeAndHeight
    private fun serializeHeightAndBiome(writeBatch: WriteBatch, chunk: UnsafeChunk) {
        //Write biomeAndHeight
        val heightAndBiomesBuffer = ByteBufAllocator.DEFAULT.ioBuffer()
        try {
            for (height in chunk.heightMapArray) {
                heightAndBiomesBuffer.writeShortLE(height.toInt())
            }
            var biomePalette: Palette<Int?>? = null
            for (ySection in chunk.provider.dimensionData.minSectionY..chunk.provider.dimensionData.maxSectionY) {
                val section = chunk.getSection(ySection) ?: continue
                section.biomes.writeToStorageRuntime(
                    heightAndBiomesBuffer,
                    { obj: V? -> obj.toInt() }, biomePalette
                )
                biomePalette = section.biomes
            }
            if (heightAndBiomesBuffer.readableBytes() > 0) {
                writeBatch.put(
                    LevelDBKeyUtil.DATA_3D.getKey(chunk.x, chunk.z, chunk.provider.dimensionData),
                    Utils.convertByteBuf2Array(heightAndBiomesBuffer)
                )
            }
        } finally {
            heightAndBiomesBuffer.release()
        }
    }

    //read biomeAndHeight
    private fun deserializeHeightAndBiome(db: DB, builder: IChunkBuilder, pnxExtraData: CompoundTag?) {
        var heightAndBiomesBuffer: ByteBuf? = null
        try {
            val dimensionInfo = builder.dimensionData
            val bytes = db[LevelDBKeyUtil.DATA_3D.getKey(
                builder.chunkX, builder.chunkZ,
                dimensionInfo!!
            )]
            if (bytes != null) {
                heightAndBiomesBuffer = Unpooled.wrappedBuffer(bytes)
                val heights = ShortArray(256)
                for (i in 0..255) {
                    heights[i] = heightAndBiomesBuffer.readShortLE()
                }
                builder.heightMap(heights)
                var lastPalette: Palette<Int?>? = null
                val minSectionY = builder.dimensionData.minSectionY
                for (y in minSectionY..builder.dimensionData.maxSectionY) {
                    val section = builder.sections[y - minSectionY] ?: continue
                    section.biomes.readFromStorageRuntime(
                        heightAndBiomesBuffer,
                        { i: Int -> i }, lastPalette!!
                    )
                    lastPalette = section.biomes
                }
            } else {
                val bytes2D = db[LevelDBKeyUtil.DATA_2D.getKey(
                    builder.chunkX, builder.chunkZ,
                    dimensionInfo
                )]
                if (bytes2D != null) {
                    heightAndBiomesBuffer = Unpooled.wrappedBuffer(bytes2D)
                    val heights = ShortArray(256)
                    for (i in 0..255) {
                        heights[i] = heightAndBiomesBuffer.readShortLE()
                    }
                    builder.heightMap(heights)
                    val biomes = ByteArray(256)
                    heightAndBiomesBuffer.readBytes(biomes)

                    val minSectionY = builder.dimensionData.minSectionY
                    for (y in minSectionY..builder.dimensionData.maxSectionY) {
                        val section = builder.sections[y - minSectionY] ?: continue
                        val biomePalette = section.biomes
                        for (x in 0..15) {
                            for (z in 0..15) {
                                for (sy in 0..15) {
                                    biomePalette[IChunk.Companion.index(x, sy, z)] = biomes[x + 16 * z].toInt()
                                }
                            }
                        }
                    }
                }
            }
        } finally {
            heightAndBiomesBuffer?.release()
        }
    }

    private fun deserializeTileAndEntity(db: DB, builder: IChunkBuilder, pnxExtraData: CompoundTag?) {
        val dimensionInfo = builder.dimensionData
        val tileBytes = db[LevelDBKeyUtil.BLOCK_ENTITIES.getKey(
            builder.chunkX, builder.chunkZ,
            dimensionInfo!!
        )]
        if (tileBytes != null) {
            val blockEntityTags: MutableList<CompoundTag> = ArrayList()
            try {
                BufferedInputStream(ByteArrayInputStream(tileBytes)).use { stream ->
                    while (stream.available() > 0) {
                        blockEntityTags.add(NBTIO.read(stream, ByteOrder.LITTLE_ENDIAN))
                    }
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
            builder.blockEntities(blockEntityTags)
        }

        val chunk_key: ByteArray = LevelDBKeyUtil.Companion.getChunkKey(
            builder.chunkX, builder.chunkZ,
            dimensionInfo
        )
        val key_buf = ByteBuffer.allocate(LevelDBKeyUtil.Companion.NEW_ENTITIES_KEY.size + chunk_key.size)
        key_buf.order(ByteOrder.LITTLE_ENDIAN)
        key_buf.put(LevelDBKeyUtil.Companion.NEW_ENTITIES_KEY)
        key_buf.put(chunk_key)
        val new_key = key_buf.array()

        val new_entity_bytes = db[new_key]
        if (new_entity_bytes == null) {
            val key = LevelDBKeyUtil.ENTITIES.getKey(
                builder.chunkX, builder.chunkZ,
                dimensionInfo
            )
            val entityBytes = db[key] ?: return
            val entityTags: MutableList<CompoundTag?> = ArrayList()
            try {
                BufferedInputStream(ByteArrayInputStream(entityBytes)).use { stream ->
                    while (stream.available() > 0) {
                        entityTags.add(NBTIO.read(stream, ByteOrder.LITTLE_ENDIAN))
                    }
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
            if (pnxExtraData == null) {
                db.delete(key)
                val list = entityTags.stream().map { obj: CompoundTag? -> BDSEntityTranslator.translate() }.filter(
                    Predicates.notNull()
                ).toList()
                builder.entities(list)
            } else {
                builder.entities(entityTags)
            }
        } else {
            val entityTags: MutableList<CompoundTag?> = ArrayList()
            try {
                BufferedInputStream(ByteArrayInputStream(new_entity_bytes)).use { stream ->
                    while (stream.available() > 0) {
                        val bytes = ByteArray(8)
                        val bytes_read = stream.read(bytes)
                        if (bytes_read != 8) {
                            throw IOException("Failed to read 8 bytes for Int64")
                        }
                        val bb = ByteBuffer.wrap(bytes)
                        bb.order(ByteOrder.LITTLE_ENDIAN)
                        val entity_id = bb.getLong()

                        val entity_key: ByteArray = LevelDBKeyUtil.Companion.getEntityKey(entity_id)
                        val entity_bytes = db[entity_key] ?: throw IOException("Failed to read entity key")
                        entityTags.add(NBTIO.read(entity_bytes, ByteOrder.LITTLE_ENDIAN))
                    }
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
            builder.entities(entityTags)
        }
    }

    private fun serializeTileAndEntity(writeBatch: WriteBatch, chunk: IChunk) {
        //Write blockEntities
        val blockEntities: Collection<BlockEntity> = chunk.blockEntities.values
        val tileBuffer = ByteBufAllocator.DEFAULT.ioBuffer()
        try {
            ByteBufOutputStream(tileBuffer).use { bufStream ->
                val key = LevelDBKeyUtil.BLOCK_ENTITIES.getKey(chunk.x, chunk.z, chunk.provider.dimensionData)
                if (blockEntities.isEmpty()) writeBatch.delete(key)
                else {
                    for (blockEntity in blockEntities) {
                        blockEntity.saveNBT()
                        NBTIO.write(blockEntity.namedTag, bufStream, ByteOrder.LITTLE_ENDIAN)
                    }
                    writeBatch.put(key, Utils.convertByteBuf2Array(tileBuffer))
                }
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        } finally {
            tileBuffer.release()
        }

        val entities: Collection<Entity> = chunk.entities.values
        val entityBuffer = ByteBufAllocator.DEFAULT.ioBuffer()
        try {
            ByteBufOutputStream(entityBuffer).use { bufStream ->
                val chunk_key: ByteArray =
                    LevelDBKeyUtil.Companion.getChunkKey(chunk.x, chunk.z, chunk.provider.dimensionData)
                val bb = ByteBuffer.allocate(LevelDBKeyUtil.Companion.NEW_ENTITIES_KEY.size + chunk_key.size)
                bb.put(LevelDBKeyUtil.Companion.NEW_ENTITIES_KEY)
                bb.put(chunk_key)
                bb.order(ByteOrder.LITTLE_ENDIAN)
                val new_key = bb.array()

                for (e in entities) {
                    if ((e !is Player) && !e.closed && e.canBeSavedWithChunk()) {
                        e.saveNBT()

                        val entity_uuid = e.getUniqueId()!!.leastSignificantBits

                        val entity_uuid_buf = ByteBuffer.allocate(java.lang.Long.BYTES)
                        entity_uuid_buf.order(ByteOrder.LITTLE_ENDIAN)
                        entity_uuid_buf.putLong(entity_uuid)
                        bufStream.write(entity_uuid_buf.array())

                        val entity_tag_bytes = NBTIO.write(e.namedTag, ByteOrder.LITTLE_ENDIAN)
                        val actor_key: ByteArray = LevelDBKeyUtil.Companion.getEntityKey(entity_uuid)
                        writeBatch.put(actor_key, entity_tag_bytes)
                    }
                }
                writeBatch.put(new_key, Utils.convertByteBuf2Array(entityBuffer))
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        } finally {
            entityBuffer.release()
        }
    }

    companion object {
        val INSTANCE: LevelDBChunkSerializer = LevelDBChunkSerializer()
    }
}
