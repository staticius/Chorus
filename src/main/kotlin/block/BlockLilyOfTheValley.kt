package org.chorus_oss.chorus.block

class BlockLilyOfTheValley : BlockFlower {
    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LILY_OF_THE_VALLEY)
    }
}