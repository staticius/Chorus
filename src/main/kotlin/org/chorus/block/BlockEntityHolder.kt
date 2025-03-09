package org.chorus.block

import org.chorus.blockentity.BlockEntity
import org.chorus.blockentity.BlockEntity.Companion.createBlockEntity
import org.chorus.level.Level
import org.chorus.level.Locator
import org.chorus.level.format.IChunk
import org.chorus.math.BlockVector3
import org.chorus.math.Vector3
import org.chorus.nbt.tag.CompoundTag
import org.chorus.utils.LevelException

interface BlockEntityHolder<E : BlockEntity?> {
    val blockEntity: E?
        get() {
            val level = level ?: throw LevelException("Undefined Level reference")
            val blockEntity = if (this is Vector3) {
                level.getBlockEntity(this as Vector3)
            } else if (this is BlockVector3) {
                level.getBlockEntity((this as BlockVector3))
            } else {
                level.getBlockEntity(BlockVector3(floorX, floorY, floorZ))
            }

            val blockEntityClass = blockEntityClass
            if (blockEntityClass.isInstance(blockEntity)) {
                return blockEntityClass.cast(blockEntity)
            }
            return null
        }

    fun createBlockEntity(): E {
        return createBlockEntity(null)
    }

    fun createBlockEntity(initialData: CompoundTag?, vararg args: Any?): E {
        var initialData = initialData
        val typeName = blockEntityType
        val chunk = chunk ?: throw LevelException("Undefined Level or chunk reference")
        initialData = initialData?.copy() ?: CompoundTag()
        val created = createBlockEntity(
            typeName, chunk,
            initialData
                .putString("id", typeName)
                .putInt("x", floorX)
                .putInt("y", floorY)
                .putInt("z", floorZ),
            *args
        )

        val entityClass = blockEntityClass

        if (!entityClass.isInstance(created)) {
            val error =
                "Failed to create the block entity " + typeName + " of class " + entityClass + " at " + locator + ", " +
                        "the created type is not an instance of the requested class. Created: " + created
            created?.close()
            throw IllegalStateException(error)
        }
        return entityClass.cast(created)
    }

    val orCreateBlockEntity: E
        get() {
            val blockEntity = blockEntity
            if (blockEntity != null) {
                return blockEntity
            }
            return createBlockEntity()
        }

    val blockEntityClass: Class<out E>

    val blockEntityType: String

    val chunk: IChunk?

    val floorX: Int

    val floorY: Int

    val floorZ: Int

    val locator: Locator

    val level: Level

    val block: Block?
        get() {
            return if (this is Block) {
                field
            } else if (this is Locator) {
                locator.levelBlock
            } else if (this is Vector3) {
                level.getBlock(vector3)
            } else {
                level.getBlock(floorX, floorY, floorZ)
            }
        }

    companion object {
        fun <E : BlockEntity?, H : BlockEntityHolder<E>?> setBlockAndCreateEntity(holder: H): E? {
            return setBlockAndCreateEntity(holder, true, true)
        }

        fun <E : BlockEntity?, H : BlockEntityHolder<E>?> setBlockAndCreateEntity(
            holder: H, direct: Boolean, update: Boolean
        ): E? {
            return setBlockAndCreateEntity(holder, direct, update, null)
        }

        fun <E : BlockEntity?, H : BlockEntityHolder<E>?> setBlockAndCreateEntity(
            holder: H, direct: Boolean, update: Boolean, initialData: CompoundTag?,
            vararg args: Any?
        ): E? {
            val block = holder!!.block
            val level = block!!.level
            val layer0 = level.getBlock(block.position, 0)
            val layer1 = level.getBlock(block.position, 1)
            if (level.setBlock(block.position, block, direct, update)) {
                try {
                    return holder.createBlockEntity(initialData, *args)
                } catch (e: Exception) {
                    Loggers.logBlocKEntityHolder.warn(
                        "Failed to create block entity {} at {} at ",
                        holder.blockEntityType,
                        holder.locator, e
                    )
                    level.setBlock(layer0!!.position, 0, layer0, direct, update)
                    level.setBlock(layer1!!.position, 1, layer1, direct, update)
                    throw e
                }
            }

            return null
        }
    }
}
