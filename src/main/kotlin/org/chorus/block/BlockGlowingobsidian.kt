package org.chorus.block

import org.chorus.item.*

class BlockGlowingobsidian @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    override val name: String
        get() = "Glowing Obsidian"

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val hardness: Double
        get() = 50.0

    override val resistance: Double
        get() = 6000.0

    override val lightLevel: Int
        get() = 12

    override fun toItem(): Item {
        return ItemBlock(get(OBSIDIAN))
    }

    override val toolTier: Int
        get() = ItemTool.TIER_DIAMOND

    override fun getDrops(item: Item): Array<Item> {
        return if (item.isPickaxe && item.tier > ItemTool.TIER_DIAMOND) {
            arrayOf(toItem())
        } else {
            Item.EMPTY_ARRAY
        }
    }

    override fun canBePushed(): Boolean {
        return false
    }

    override fun canBePulled(): Boolean {
        return false
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.GLOWINGOBSIDIAN)

    }
}