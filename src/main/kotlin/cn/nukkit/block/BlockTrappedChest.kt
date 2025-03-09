package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.blockentity.BlockEntity
import cn.nukkit.blockentity.BlockEntityChest
import cn.nukkit.item.*
import cn.nukkit.math.BlockFace
import cn.nukkit.math.BlockFace.Companion.fromHorizontalIndex
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.nbt.tag.Tag
import kotlin.math.min

class BlockTrappedChest @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockChest(blockstate) {
    override val name: String
        get() = "Trapped Chest"

    override fun place(
        item: Item,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player?
    ): Boolean {
        blockFace = if (player != null) fromHorizontalIndex(
            player.getDirection()!!.getOpposite()!!.horizontalIndex
        ) else BlockFace.SOUTH

        var chest: BlockEntityChest? = null

        for (side in BlockFace.Plane.HORIZONTAL) {
            if ((blockFace == BlockFace.WEST || blockFace == BlockFace.EAST) && (side == BlockFace.WEST || side == BlockFace.EAST)) {
                continue
            } else if ((blockFace == BlockFace.NORTH || blockFace == BlockFace.SOUTH) && (side == BlockFace.NORTH || side == BlockFace.SOUTH)) {
                continue
            }
            val c = this.getSide(side)
            if (c is BlockTrappedChest && c.blockFace == blockFace) {
                val blockEntity = level.getBlockEntity(c.position)
                if (blockEntity is BlockEntityChest && !blockEntity.isPaired) {
                    chest = blockEntity
                    break
                }
            }
        }

        level.setBlock(block.position, this, true, true)
        val nbt = CompoundTag()
            .putList("Items", ListTag<Tag?>())
            .putString("id", BlockEntity.CHEST)
            .putInt("x", position.x.toInt())
            .putInt("y", position.y.toInt())
            .putInt("z", position.z.toInt())

        if (item.hasCustomName()) {
            nbt!!.putString("CustomName", item.customName)
        }

        if (item.hasCustomBlockData()) {
            val customData: Map<String?, Tag?> = item.customBlockData!!.getTags()
            for ((key, value) in customData) {
                nbt!!.put(key, value)
            }
        }

        val blockEntity = BlockEntity.createBlockEntity(
            BlockEntity.CHEST,
            level.getChunk(
                position.x.toInt() shr 4,
                position.z.toInt() shr 4
            ),
            nbt
        ) as BlockEntityChest?
            ?: return false

        if (chest != null) {
            chest.pairWith(blockEntity)
            blockEntity.pairWith(chest)
        }

        return true
    }

    override fun getWeakPower(face: BlockFace?): Int {
        var playerCount = 0

        val blockEntity = level.getBlockEntity(this.position)

        if (blockEntity is BlockEntityChest) {
            playerCount = blockEntity.getInventory().getViewers().size
        }

        return min(playerCount.toDouble(), 15.0).toInt()
    }

    override fun getStrongPower(side: BlockFace?): Int {
        return if (side == BlockFace.UP) this.getWeakPower(side) else 0
    }

    override val isPowerSource: Boolean
        get() = true

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.TRAPPED_CHEST, CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION)
            get() = Companion.field
    }
}
