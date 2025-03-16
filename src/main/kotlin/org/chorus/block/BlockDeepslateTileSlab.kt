package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.ItemTool

class BlockDeepslateTileSlab @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockSlab(blockstate, DEEPSLATE_TILE_DOUBLE_SLAB) {
    override fun getSlabName(): String {
        return "Deepslate Tile"
    }

    override val hardness: Double
        get() = 3.5

    override val resistance: Double
        get() = 6.0

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override fun isSameType(slab: BlockSlab): Boolean {
        return id == slab.id
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.DEEPSLATE_TILE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)

    }
}