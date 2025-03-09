package cn.nukkit.block

import cn.nukkit.item.*
import cn.nukkit.tags.BlockTags
import java.util.Set

class BlockBlueShulkerBox @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockUndyedShulkerBox(blockstate) {
    override fun getShulkerBox(): Item {
        return ItemShulkerBox(11)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BLUE_SHULKER_BOX, Set.of(BlockTags.PNX_SHULKERBOX))
            get() = Companion.field
    }
}