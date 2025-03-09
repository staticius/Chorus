package org.chorus.block

import org.chorus.item.ItemTool

class BlockBambooPlanks @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    override val name: String
        get() = "Bamboo Planks"

    override val hardness: Double
        get() = 2.0

    override val resistance: Double
        get() = 15.0

    override val burnChance: Int
        get() = 5

    override val burnAbility: Int
        get() = 20

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    companion object {
        val properties: BlockProperties = BlockProperties(BAMBOO_PLANKS)
            get() = Companion.field
    }
}