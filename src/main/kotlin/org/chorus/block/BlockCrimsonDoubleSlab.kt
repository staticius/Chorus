package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.*

class BlockCrimsonDoubleSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockDoubleSlabBase(blockstate) {
    override fun getSlabName(): String {
        return "Crimson"
    }

    override fun isCorrectTool(item: Item): Boolean {
        return true
    }

    override val hardness: Double
        get() = 2.0

    override val resistance: Double
        get() = 3.0

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    override val burnChance: Int
        get() = 0

    override val burnAbility: Int
        get() = 0

    override fun getSingleSlab(): BlockState {
        return BlockCrimsonSlab.Companion.PROPERTIES.getDefaultState()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(CRIMSON_DOUBLE_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}