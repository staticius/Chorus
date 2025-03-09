package cn.nukkit.block

import cn.nukkit.entity.Entity
import cn.nukkit.entity.Entity.Companion.createEntity
import cn.nukkit.entity.item.EntityFallingBlock
import cn.nukkit.event.block.BlockFallEvent
import cn.nukkit.item.*
import cn.nukkit.level.Level
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.FloatTag
import cn.nukkit.nbt.tag.ListTag

/**
 * @author rcsuperman (Nukkit Project)
 */
abstract class BlockFallable(blockstate: BlockState?) : BlockSolid(blockstate) {
    override fun onUpdate(type: Int): Int {
        val down = this.down()
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if ((down!!.isAir || down is BlockFire || down is BlockLiquid ||
                        (down is BlockBubbleColumn && down.getLevelBlockAtLayer(1) is BlockLiquid))
            ) {
                val event = BlockFallEvent(this)
                level.server.pluginManager.callEvent(event)
                if (event.isCancelled) {
                    return type
                }

                drop(CompoundTag())
            }
            return type
        }
        return 0
    }

    fun drop(customNbt: CompoundTag) {
        level.setBlock(this.position, get(Block.AIR), true, true)
        val fall = createFallingEntity(customNbt)

        fall!!.spawnToAll()
    }

    protected open fun createFallingEntity(customNbt: CompoundTag): EntityFallingBlock? {
        val nbt = CompoundTag()
            .putList(
                "Pos", ListTag<FloatTag?>()
                    .add(FloatTag(position.x + 0.5))
                    .add(FloatTag(position.y))
                    .add(FloatTag(position.z + 0.5))
            )
            .putList(
                "Motion", ListTag<FloatTag?>()
                    .add(FloatTag(0f))
                    .add(FloatTag(0f))
                    .add(FloatTag(0f))
            )
            .putList(
                "Rotation", ListTag<FloatTag?>()
                    .add(FloatTag(0f))
                    .add(FloatTag(0f))
            )
            .putCompound("Block", blockState!!.blockStateTag.copy())

        for ((key, value) in customNbt.entrySet) {
            nbt!!.put(key, value!!.copy())
        }

        val fall = createEntity(
            Entity.FALLING_BLOCK,
            level.getChunk(position.x.toInt() shr 4, position.z.toInt() shr 4), nbt
        ) as EntityFallingBlock?

        fall?.spawnToAll()

        return fall
    }

    open fun toFallingItem(): Item? {
        return this.toItem()
    }
}
