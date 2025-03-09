package org.chorus.level.format

import cn.nukkit.block.Block
import cn.nukkit.block.BlockAir
import cn.nukkit.block.BlockState
import cn.nukkit.blockentity.BlockEntity
import cn.nukkit.entity.Entity
import cn.nukkit.level.DimensionData
import cn.nukkit.nbt.tag.CompoundTag
import org.jetbrains.annotations.ApiStatus

class UnsafeChunk(private val chunk: Chunk) {
    @get:ApiStatus.Internal
    val sections: Array<ChunkSection?>?
        get() = chunk.sections

    val dimensionData: DimensionData?
        get() = chunk.dimensionData

    val blockEntities: Map<Long?, BlockEntity>
        get() = chunk.tiles

    private fun setChanged() {
        chunk.changes.incrementAndGet()
    }

    private fun setChanged(changed: Boolean) {
        if (changed) {
            setChanged()
        } else {
            chunk.changes.set(0)
        }
    }

    fun populateSkyLight() {
        // basic light calculation
        for (z in 0..15) {
            for (x in 0..15) { // iterating over all columns in chunk
                val top = this.getHeightMap(x, z) // top-most block
                var y = dimensionData.getMaxHeight()
                while (y > top) {
                    // all the blocks above & including the top-most block in a column are exposed to sun and
                    // thus have a skylight value of 15
                    this.setBlockSkyLight(x, y, z, 15)
                    --y
                }

                var light = 15 // light value that will be applied starting with the next block
                var nextDecrease = 0 // decrease that that will be applied starting with the next block

                y = top
                while (y >= dimensionData.getMinHeight()) {
                    // going under the top-most block
                    light -= nextDecrease // this light value will be applied for this block. The following checks are all about the next blocks

                    if (light < 0) {
                        light = 0
                    }

                    this.setBlockSkyLight(x, y, z, light)

                    if (light == 0) { // skipping block checks, because everything under a block that has a skylight value
                        // of 0 also has a skylight value of 0
                        --y
                        continue
                    }

                    // START of checks for the next block
                    val block = getBlockState(x, y, z)!!.toBlock()

                    if (!block.isTransparent) { // if we encounter an opaque block, all the blocks under it will
                        // have a skylight value of 0 (the block itself has a value of 15, if it's a top-most block)
                        light = 0
                    } else if (block.diffusesSkyLight()) {
                        nextDecrease += 1 // skylight value decreases by one for each block under a block
                        // that diffuses skylight. The block itself has a value of 15 (if it's a top-most block)
                    } else {
                        nextDecrease += block.lightFilter // blocks under a light filtering block will have a skylight value
                        // decreased by the lightFilter value of that block. The block itself
                        // has a value of 15 (if it's a top-most block)
                    }
                    --y
                }
            }
        }
    }

    /**
     * Gets or create section.
     *
     * @param sectionY the section y range -4 ~ 19
     * @return the or create section
     */
    private fun getOrCreateSection(sectionY: Int): ChunkSection? {
        val minSectionY = dimensionData.getMinSectionY()
        val offsetY = sectionY - minSectionY
        if (offsetY < 0) return null
        for (i in 0..offsetY) {
            if (chunk.sections[i] == null) {
                chunk.sections[i] = ChunkSection((i + minSectionY).toByte())
            }
        }
        return chunk.sections[offsetY]
    }

    fun getSection(fY: Int): ChunkSection? {
        return chunk.sections[fY - dimensionData.getMinSectionY()]
    }

    fun setSection(fY: Int, section: ChunkSection?) {
        chunk.sections[fY - dimensionData.getMinSectionY()] = section
        setChanged()
    }

    fun getBlockState(x: Int, y: Int, z: Int): BlockState? {
        val section = getSection(y shr 4) ?: return BlockAir.STATE
        return section.getBlockState(x, y and 0x0f, z, 0)
    }

    fun getBlockState(x: Int, y: Int, z: Int, layer: Int): BlockState? {
        val section = getSection(y shr 4) ?: return BlockAir.STATE
        return section.getBlockState(x, y and 0x0f, z, layer)
    }

    fun getAndSetBlockState(x: Int, y: Int, z: Int, blockstate: BlockState?, layer: Int): BlockState? {
        return getOrCreateSection(y shr 4)!!.getAndSetBlockState(x, y and 0x0f, z, blockstate, layer)
    }

    fun setBlockState(x: Int, y: Int, z: Int, blockstate: BlockState?, layer: Int) {
        getOrCreateSection(y shr 4)!!.setBlockState(x, y and 0x0f, z, blockstate, layer)
    }

    fun getBlockSkyLight(x: Int, y: Int, z: Int): Int {
        val section = getSection(y shr 4) ?: return 0
        return section.getBlockSkyLight(x, y and 0x0f, z).toInt()
    }

    fun setBlockSkyLight(x: Int, y: Int, z: Int, level: Int) {
        val section = getOrCreateSection(y shr 4)
        section?.setBlockSkyLight(x, y and 0x0f, z, level.toByte())
    }

    fun getBlockLight(x: Int, y: Int, z: Int): Int {
        val section = getSection(y shr 4) ?: return 0
        return section.getBlockLight(x, y and 0x0f, z).toInt()
    }

    fun setBlockLight(x: Int, y: Int, z: Int, level: Int) {
        getOrCreateSection(y shr 4)!!.setBlockLight(x, y and 0x0f, z, level.toByte())
    }

    /**
     * Gets highest block in this (x,z)
     *
     * @param x the x 0~15
     * @param z the z 0~15
     */
    fun getHighestBlockAt(x: Int, z: Int): Int {
        for (y in dimensionData.getMaxHeight() downTo dimensionData.getMinHeight()) {
            if (getBlockState(x, y, z) !== BlockAir.properties.getBlockState()) {
                this.setHeightMap(x, z, y)
                return y
            }
        }
        return dimensionData.getMinHeight()
    }

    /**
     * Recalculate height map for this chunk
     */
    fun recalculateHeightMapColumn(x: Int, z: Int): Int {
        val max = getHighestBlockAt(x, z)
        var y = max
        while (y >= 0) {
            val blockState = getBlockState(x, y, z, 0)
            val block = Block.get(blockState)
            if (block.lightFilter > 1 || block.diffusesSkyLight()) {
                break
            }
            --y
        }
        setHeightMap(x, z, y)
        return y
    }

    fun recalculateHeightMap() {
        for (z in 0..15) {
            for (x in 0..15) {
                this.recalculateHeightMapColumn(x, z)
            }
        }
    }

    fun getHeightMap(x: Int, z: Int): Int {
        return chunk.heightMap[(z shl 4) or x] + dimensionData.getMinHeight()
    }

    fun setHeightMap(x: Int, z: Int, value: Int) {
        chunk.heightMap[(z shl 4) or x] = (value - dimensionData.getMinHeight()).toShort()
    }

    fun getBiomeId(x: Int, y: Int, z: Int): Int {
        val section = getSection(y shr 4) ?: return 0
        return section.getBiomeId(x, y and 0x0f, z)
    }

    fun setBiomeId(x: Int, y: Int, z: Int, biomeId: Int) {
        setChanged()
        getOrCreateSection(y shr 4)!!.setBiomeId(x, y and 0x0f, z, biomeId)
    }

    val heightMapArray: ShortArray?
        get() = chunk.heightMap

    fun isSectionEmpty(fY: Int): Boolean {
        return chunk.sections[fY - dimensionData.getMinSectionY()].isEmpty
    }

    var x: Int
        get() = chunk.x
        set(x) {
            chunk.x = x
        }

    var z: Int
        get() = chunk.z
        set(z) {
            chunk.z = z
        }

    val index: Long
        get() = chunk.index

    val provider: LevelProvider?
        get() = chunk.getProvider()

    var isLightPopulated: Boolean
        get() = chunk.isLightPopulated
        set(value) {
            chunk.isLightPopulated = value
        }

    fun setLightPopulated() {
        chunk.setLightPopulated()
    }

    var chunkState: ChunkState?
        get() = chunk.getChunkState()
        set(chunkState) {
            chunk.setChunkState(chunkState!!)
        }

    fun addEntity(entity: Entity) {
        chunk.addEntity(entity)
    }

    fun removeEntity(entity: Entity) {
        chunk.removeEntity(entity)
    }

    fun addBlockEntity(blockEntity: BlockEntity) {
        chunk.addBlockEntity(blockEntity)
    }

    fun removeBlockEntity(blockEntity: BlockEntity) {
        chunk.removeBlockEntity(blockEntity)
    }

    val entities: Map<Long?, Entity?>?
        get() = chunk.getEntities()

    fun getTile(x: Int, y: Int, z: Int): BlockEntity? {
        return chunk.getTile(x, y, z)
    }

    val extraData: CompoundTag?
        get() = chunk.getExtraData()

    fun hasChanged(): Boolean {
        return chunk.hasChanged()
    }

    val changes: Long
        get() = chunk.getChanges()

    fun setPosition(x: Int, z: Int) {
        chunk.setPosition(x, z)
    }

    val isOverWorld: Boolean
        get() = chunk.isOverWorld

    val isNether: Boolean
        get() = chunk.isNether

    val isTheEnd: Boolean
        get() = chunk.isTheEnd

    val isGenerated: Boolean
        get() = chunk.isGenerated

    val isPopulated: Boolean
        get() = chunk.isPopulated

    fun setGenerated() {
        chunk.setGenerated()
    }

    fun setPopulated() {
        chunk.setPopulated()
    }
}
