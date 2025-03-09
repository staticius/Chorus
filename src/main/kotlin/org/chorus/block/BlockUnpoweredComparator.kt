package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockUnpoweredComparator @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockRedstoneComparator(blockstate) {
    override val name: String
        get() = "Comparator Block Unpowered"

    override val unpowered: BlockRedstoneComparator
        get() = this

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.UNPOWERED_COMPARATOR,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.OUTPUT_LIT_BIT,
            CommonBlockProperties.OUTPUT_SUBTRACT_BIT
        )
            get() = Companion.field
    }
}