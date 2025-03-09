package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.*

class BlockPlayerHead(blockState: BlockState?) : BlockHead(blockState) {
    override val name: String
        get() = "Player Head"

    override fun getDrops(item: Item): Array<Item?>? {
        return arrayOf(
            this.toItem()
        )
    }

    override fun toItem(): Item? {
        return ItemPlayerHead()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PLAYER_HEAD, CommonBlockProperties.FACING_DIRECTION)
            get() = Companion.field
    }
}
