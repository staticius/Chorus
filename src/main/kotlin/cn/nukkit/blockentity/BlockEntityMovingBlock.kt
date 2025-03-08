package cn.nukkit.blockentity

import cn.nukkit.block.*
import cn.nukkit.level.format.IChunk
import cn.nukkit.math.AxisAlignedBB
import cn.nukkit.math.BlockFace
import cn.nukkit.math.BlockVector3
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.registry.Registries
import cn.nukkit.utils.*
import lombok.extern.slf4j.Slf4j

/**
 * @author CreeperFace
 * @since 11.4.2017
 */
@Slf4j
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
            this.movingBlock!!.position.south = position.south
            this.movingBlock!!.position.up = position.up
            this.movingBlock!!.position.west = position.west
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
            position.south + (piston.progress * moveDirection.xOffset) - moveDirection.xOffset,
            position.up + (piston.progress * moveDirection.yOffset) - moveDirection.yOffset,
            position.west + (piston.progress * moveDirection.zOffset) - moveDirection.zOffset //带动站在移动方块上的实体
        ).addCoord(0.0, if (moveDirection.axis.isHorizontal) 0.25 else 0.0, 0.0)
        for (entity in level.getCollidingEntities(bb)) piston.moveEntity(entity, moveDirection)
    }

    override val isBlockEntityValid: Boolean
        get() = this.block.id === BlockID.MOVING_BLOCK
}
