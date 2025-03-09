package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.*

class BlockCreeperHead(blockState: BlockState?) : BlockHead(blockState) {
    override val name: String
        get() = "Creeper Head"

    override fun getDrops(item: Item): Array<Item?>? {
        return arrayOf(
            this.toItem()
        )
    }

    override fun toItem(): Item? {
        return ItemCreeperHead()
    }

    companion object {
        val properties: BlockProperties = BlockProperties(CREEPER_HEAD, CommonBlockProperties.FACING_DIRECTION)
            get() = Companion.field
    }
}
