package cn.nukkit.block

import cn.nukkit.item.*
import cn.nukkit.tags.BlockTags
import java.util.Set

class BlockPurpleShulkerBox @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockUndyedShulkerBox(blockstate) {
    override fun getShulkerBox(): Item {
        return ItemShulkerBox(10)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PURPLE_SHULKER_BOX, Set.of(BlockTags.PNX_SHULKERBOX))
            get() = Companion.field
    }
}