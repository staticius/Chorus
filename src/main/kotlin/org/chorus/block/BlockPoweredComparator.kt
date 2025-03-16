package org.chorus.block

import org.chorus.block.property.CommonBlockProperties

class BlockPoweredComparator @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockRedstoneComparator(blockstate) {
    init {
        this.isPowered = true
    }

    override val name: String
        get() = "Comparator Block Powered"

    override fun getPowered(): BlockRedstoneComparator {
        return this
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.POWERED_COMPARATOR,
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
            CommonBlockProperties.OUTPUT_LIT_BIT,
            CommonBlockProperties.OUTPUT_SUBTRACT_BIT
        )

    }
}