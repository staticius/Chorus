package org.chorus.block

import cn.nukkit.item.ItemTool

class BlockBrickBlock @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    override val hardness: Double
        get() = 2.0

    override val resistance: Double
        get() = 6.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    companion object {
        val properties: BlockProperties = BlockProperties(BRICK_BLOCK)
            get() = Companion.field
    }
}