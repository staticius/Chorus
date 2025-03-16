package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.*

class BlockPaleMossCarpet @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockMossCarpet(blockstate) {
    override val name: String
        get() = "Pale Moss Carpet"

    override val resistance: Double
        get() = 0.1

    override fun getDrops(item: Item): Array<Item> {
        return arrayOf(toItem())
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.PALE_MOSS_CARPET,
            CommonBlockProperties.PALE_MOSS_CARPET_SIDE_EAST,
            CommonBlockProperties.PALE_MOSS_CARPET_SIDE_NORTH,
            CommonBlockProperties.PALE_MOSS_CARPET_SIDE_SOUTH,
            CommonBlockProperties.PALE_MOSS_CARPET_SIDE_WEST,
            CommonBlockProperties.UPPER_BLOCK_BIT
        )

    }
}
