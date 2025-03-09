package org.chorus.level

import org.chorus.block.*
import org.chorus.blockentity.BlockEntity
import org.chorus.level.format.IChunk
import org.chorus.math.Vector3
import lombok.extern.slf4j.Slf4j

@Slf4j
open class Locator(@JvmField var position: Vector3, @JvmField var level: Level) : NamedPosition, Cloneable, IVector3 {
    constructor(level: Level) : this(0.0, 0.0, 0.0, level)

    constructor(x: Double, y: Double, z: Double, level: Level) : this(Vector3(x, y, z), level)

    val floorX: Int
        get() = position.floorX

    val floorY: Int
        get() = position.floorY

    val floorZ: Int
        get() = position.floorZ

    val vector3: Vector3
        get() = position.clone()

    val locator: Locator
        get() = this.clone()

    @SneakyThrows
    override fun clone(): Locator {
        return super.clone() as Locator
    }

    fun setLevel(level: Level): Locator {
        this.level = level
        return this
    }

    open fun getSide(face: BlockFace): Locator? {
        return this.getSide(face, 1)
    }

    open fun getSide(face: BlockFace, step: Int): Locator? {
        return fromObject(position.getSide(face, step), level)
    }

    fun getSidePos(face: BlockFace): Locator {
        return fromObject(position.getSide(face, 1), level)
    }

    override fun toString(): String {
        return "Locator(level=" + level.name + ",position=" + this.position + ")"
    }

    open val levelBlockEntity: BlockEntity?
        get() = level.getBlockEntity(this.position)

    fun <T : BlockEntity?> getTypedBlockEntity(type: Class<T>): T? {
        val blockEntity = level.getBlockEntity(this.position)
        return if (type.isInstance(blockEntity)) type.cast(blockEntity) else null
    }

    val levelBlockState: BlockState
        get() = getLevelBlockState(0)

    fun getLevelBlockState(layer: Int): BlockState {
        return level.getBlockStateAt(
            position.floorX,
            position.floorY, position.floorZ, layer
        )!!
    }

    val levelBlock: Block?
        get() = getLevelBlock(true)

    fun getLevelBlock(load: Boolean): Block? {
        return level.getBlock(this.position, load)
    }

    fun getLevelBlock(layer: Int): Block? {
        return level.getBlock(this.position, layer)
    }

    fun getLevelBlock(layer: Int, load: Boolean): Block? {
        return level.getBlock(this.position, layer, load)
    }

    val tickCachedLevelBlock: Block?
        get() = level.getTickCachedBlock(this.position)

    val levelBlockAround: Set<Block?>
        get() = level.getBlockAround(this.position)

    fun getLevelBlockAtLayer(layer: Int): Block? {
        return level.getBlock(this.position, layer)
    }

    fun getTickCachedLevelBlockAtLayer(layer: Int): Block? {
        return level.getTickCachedBlock(this.position, layer)
    }

    val levelName: String
        get() = level.name!!

    val x: Double
        get() = position.x

    val y: Double
        get() = position.y

    val z: Double
        get() = position.z

    val chunk: IChunk?
        get() = level.getChunk(position.chunkX, position.chunkZ)

    open fun add(x: Double, y: Double, z: Double): Locator {
        return Locator(position.add(x, y, z), this.level)
    }

    open fun subtract(x: Double, y: Double, z: Double): Locator {
        return this.add(-x, -y, -z)
    }

    companion object {
        @JvmStatic
        fun fromObject(pos: Vector3, level: Level): Locator {
            return Locator(pos.x, pos.y, pos.z, level)
        }
    }
}
