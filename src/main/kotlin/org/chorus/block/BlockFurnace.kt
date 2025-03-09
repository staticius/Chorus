package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

/**
 * @author Angelic47 (Nukkit Project)
 */
class BlockFurnace @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockLitFurnace(blockstate) {
    override val name: String
        get() = "Furnace"

    override val lightLevel: Int
        get() = 0

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(FURNACE, CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION)
            get() = Companion.field
    }
}
