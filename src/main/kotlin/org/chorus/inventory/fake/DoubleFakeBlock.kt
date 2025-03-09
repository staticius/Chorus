package org.chorus.inventory.fake

import org.chorus.Player
import org.chorus.block.Block
import org.chorus.math.Vector3
import org.chorus.nbt.tag.CompoundTag
import com.google.common.collect.Lists

class DoubleFakeBlock : SingleFakeBlock {
    constructor(blockId: String?) : super(Block.get(blockId), "default")

    constructor(blockId: String?, tileId: String?) : super(Block.get(blockId), tileId)

    constructor(block: Block, tileId: String?) : super(block, tileId)

    override fun getPlacePositions(player: Player): List<Vector3?> {
        val blockPosition = this.getOffset(player)
        if ((blockPosition!!.floorX and 1) == 1) {
            return Lists.newArrayList(blockPosition, blockPosition.east())
        }
        return Lists.newArrayList(blockPosition, blockPosition.west())
    }

    override fun getBlockEntityDataAt(position: Vector3, title: String): CompoundTag {
        return super.getBlockEntityDataAt(position, title)
            .putInt("pairx", position.floorX + (if ((position.floorX and 1) == 1) 1 else -1))
            .putInt("pairz", position.floorZ)
    }
}

