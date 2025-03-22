package org.chorus.block

class BlockHardLimeStainedGlass(blockstate: BlockState) : Block(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_LIME_STAINED_GLASS)
    }
}