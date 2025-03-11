package org.chorus.blockentity

import org.chorus.block.*
import org.chorus.level.format.IChunk
import org.chorus.math.AxisAlignedBB
import org.chorus.math.BlockFace
import org.chorus.math.BlockVector3
import org.chorus.nbt.tag.CompoundTag
import org.chorus.registry.Registries
import org.chorus.utils.*


/**
 * @author CreeperFace
 * @since 11.4.2017
 */

class BlockEntityMovingBlock(chunk: IChunk, nbt: CompoundTag) : BlockEntitySpawnable(chunk, nbt) {
    var movingBlock: Block? = null
        protected set
    protected var piston: BlockVector3? = null

    //true if the piston is extending instead of withdrawing.
    protected var expanding: Boolean = false

    /**
     * {
     * expanding: 0b,
     * id: MovingBlock,
     * isMovable: 1b,
     * movingBlock: {
     * name: "minecraft:air",
     * states: {
     * },
     * version: 18100737
     * },
     * movingBlockExtra: {
     * name: "minecraft:air",
     * states: {
     * },
     * version: 18100737
     * },
     * pistonPosX: 0,
     * pistonPosY: -1,
     * pistonPosZ: 0,
     * x: 0,
     * y: 100,
     * z: 0
     * }
     */
    override fun loadNBT() {
        super.loadNBT()
        if (namedTag.contains("movingBlock")) {
            val movingBlock = namedTag.getCompound("movingBlock")
            val blockhash = HashUtils.fnv1a_32_nbt_palette(movingBlock)
            val blockState = Registries.BLOCKSTATE[blockhash]
            if (blockState == null) {
                BlockEntityMovingBlock.log.error("Can't load moving block {}", movingBlock.toSNBT())
            } else {
                this.movingBlock = blockState.toBlock()
            }
            this.movingBlock!!.position.x = position.x
            this.movingBlock!!.position.y = position.y
            this.movingBlock!!.position.z = position.z
        } else {
            this.close()
        }

        if (namedTag.contains("pistonPosX") && namedTag.contains("pistonPosY") && namedTag.contains("pistonPosZ")) {
            this.piston = BlockVector3(
                namedTag.getInt("pistonPosX"),
                namedTag.getInt("pistonPosY"),
                namedTag.getInt("pistonPosZ")
            )
        } else {
            this.piston = BlockVector3(0, -1, 0)
        }
    }

    val movingBlockEntityCompound: CompoundTag?
        get() {
            if (namedTag.contains("movingEntity")) {
                return namedTag.getCompound("movingEntity")
            }

            return null
        }

    fun moveCollidedEntities(piston: BlockEntityPistonArm, moveDirection: BlockFace) {
        var bb: AxisAlignedBB? = movingBlock!!.boundingBox ?: return
        bb = bb.getOffsetBoundingBox(
            position.x + (piston.progress * moveDirection.xOffset) - moveDirection.xOffset,
            position.y + (piston.progress * moveDirection.yOffset) - moveDirection.yOffset,
            position.z + (piston.progress * moveDirection.zOffset) - moveDirection.zOffset //带动站在移动方块上的实体
        ).addCoord(0.0, if (moveDirection.axis.isHorizontal) 0.25 else 0.0, 0.0)
        for (entity in level.getCollidingEntities(bb)) piston.moveEntity(entity, moveDirection)
    }

    override val isBlockEntityValid: Boolean
        get() = this.block.id === BlockID.MOVING_BLOCK
}
