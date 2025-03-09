package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.*

class BlockDragonHead(blockState: BlockState?) : BlockHead(blockState) {
    override val name: String
        get() = "Dragon Head"

    override fun getDrops(item: Item): Array<Item?>? {
        return arrayOf(
            this.toItem()
        )
    }

    override fun toItem(): Item? {
        return ItemDragonHead()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(DRAGON_HEAD, CommonBlockProperties.FACING_DIRECTION)
            get() = Companion.field
    }
}
