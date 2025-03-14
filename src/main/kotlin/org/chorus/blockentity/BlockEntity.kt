package org.chorus.blockentity

import org.chorus.Server
import org.chorus.block.*
import org.chorus.level.Locator
import org.chorus.level.format.IChunk
import org.chorus.math.*
import org.chorus.nbt.tag.CompoundTag
import org.chorus.registry.Registries
import org.chorus.scheduler.Task
import org.chorus.utils.ChunkException


/**
 * @author MagicDroidX
 */

abstract class BlockEntity(chunk: IChunk, nbt: CompoundTag) : Locator(chunk.provider.level),
    BlockEntityID {
    @JvmField
    var chunk: IChunk?
    open var name: String

    @JvmField
    var id: Long
    var isMovable: Boolean

    @JvmField
    var closed: Boolean = false

    @JvmField
    var namedTag: CompoundTag
    protected var server: Server

    init {
        if (chunk.provider == null || chunk.provider.level == null) {
            throw ChunkException("Invalid garbage Chunk given to Block Entity")
        }

        this.server = Server.instance
        this.chunk = chunk
        this.setLevel(chunk.provider.level)
        this.namedTag = nbt
        this.name = ""
        this.id = count++
        position.x = namedTag.getInt("x").toDouble()
        position.y = namedTag.getInt("y").toDouble()
        position.z = namedTag.getInt("z").toDouble()
        this.isMovable = namedTag.getBoolean("isMovable")


        this.initBlockEntity()

        check(!closed) { "Could not create the entity " + javaClass.name + ", the initializer closed it on construction." }

        this.chunk!!.addBlockEntity(this)
        level.addBlockEntity(this)
    }

    protected open fun initBlockEntity() {
        loadNBT()
    }

    /**
     * 从方块实体的namedtag中读取数据
     */
    open fun loadNBT() {
    }

    /**
     * 存储方块实体数据到namedtag
     */
    open fun saveNBT() {
        namedTag.putString(
            TAG_ID,
            saveId
        )
        namedTag.putInt(TAG_X, this.x.toInt())
        namedTag.putInt(TAG_Y, this.y.toInt())
        namedTag.putInt(TAG_Z, this.z.toInt())
        namedTag.putBoolean(TAG_IS_MOVABLE, this.isMovable)
    }

    val saveId: String
        get() = Registries.BLOCKENTITY.getSaveId(javaClass)

    open val cleanedNBT: CompoundTag?
        get() {
            this.saveNBT()
            val tag = namedTag.copy()
            tag.remove("x").remove("y").remove("z").remove("id")
            return if (!tag.tags.isEmpty()) {
                tag
            } else {
                null
            }
        }

    val block: Block
        get() = this.levelBlock

    @JvmField
    abstract val isBlockEntityValid: Boolean

    open fun onUpdate(): Boolean {
        return false
    }

    fun scheduleUpdate() {
        level.scheduleBlockEntityUpdate(this)
    }

    open fun close() {
        if (!this.closed) {
            this.closed = true
            if (this.chunk != null) {
                chunk!!.removeBlockEntity(this)
            }
            if (this.level != null) {
                level.removeBlockEntity(this)
            }
            this.level = null
        }
    }

    open fun onBreak(isSilkTouch: Boolean) {
    }

    open fun setDirty() {
        chunk!!.setChanged()

        if (!this.levelBlock.isAir) {
            level.scheduler.scheduleTask(object : Task() {
                override fun onRun(currentTick: Int) {
                    if (this.isBlockEntityValid) {
                        level.updateComparatorOutputLevelSelective(
                            this@BlockEntity.position,
                            this.isObservable
                        )
                    }
                }
            })
        }
    }

    open val isObservable: Boolean
        /**
         * Indicates if an observer blocks that are looking at this block should blink when [.setDirty] is called.
         */
        get() = true

    override fun getLevelBlockEntity(): BlockEntity? {
        return super.getLevelBlockEntity()
    }

    companion object {
        const val TAG_CUSTOM_NAME: String = "CustomName"
        const val TAG_ID: String = "id"
        const val TAG_IS_MOVABLE: String = "isMovable"
        const val TAG_X: String = "x"
        const val TAG_Y: String = "y"
        const val TAG_Z: String = "z"

        var count: Long = 1
        fun createBlockEntity(type: String, locator: Locator, vararg args: Any?): BlockEntity {
            return createBlockEntity(type, locator, getDefaultCompound(locator.position, type), *args)!!
        }


        fun createBlockEntity(type: String, pos: Locator, nbt: CompoundTag?, vararg args: Any?): BlockEntity? {
            return createBlockEntity(
                type,
                pos.level.getChunk(pos.position.floorX shr 4, pos.position.floorZ shr 4),
                nbt,
                *args
            )
        }

        @JvmStatic
        fun createBlockEntity(type: String, chunk: IChunk?, nbt: CompoundTag?, vararg args: Any?): BlockEntity? {
            var blockEntity: BlockEntity? = null

            val clazz = Registries.BLOCKENTITY[type]
            if (clazz != null) {
                var exceptions: MutableList<Exception?>? = null

                for (constructor in clazz.constructors) {
                    if (blockEntity != null) {
                        break
                    }

                    if (constructor.parameterCount != (if (args == null) 2 else args.size + 2)) {
                        continue
                    }

                    try {
                        if (args == null || args.size == 0) {
                            blockEntity = constructor.newInstance(chunk, nbt) as BlockEntity
                        } else {
                            val objects = arrayOfNulls<Any>(args.size + 2)

                            objects[0] = chunk
                            objects[1] = nbt
                            System.arraycopy(args, 0, objects, 2, args.size)
                            blockEntity = constructor.newInstance(*objects) as BlockEntity
                        }
                    } catch (e: Exception) {
                        if (exceptions == null) {
                            exceptions = ArrayList()
                        }
                        exceptions.add(e)
                    }
                }
                if (blockEntity == null) {
                    val cause: Exception = IllegalArgumentException(
                        "Could not create a block entity of type $type",
                        if (exceptions != null && exceptions.size > 0) exceptions[0] else null
                    )
                    if (exceptions != null && exceptions.size > 1) {
                        for (i in 1..<exceptions.size) {
                            cause.addSuppressed(exceptions[i])
                        }
                    }
                    BlockEntity.log.error(
                        "Could not create a block entity of type {} with {} args", type,
                        args.size, cause
                    )
                }
            } else {
                BlockEntity.log.debug("Block entity type {} is unknown", type)
            }


            return blockEntity
        }

        @JvmStatic
        fun getDefaultCompound(pos: Vector3, id: String): CompoundTag {
            return CompoundTag()
                .putString("id", id)
                .putInt("x", pos.floorX)
                .putInt("y", pos.floorY)
                .putInt("z", pos.floorZ)
        }
    }
}
