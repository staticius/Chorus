package org.chorus.block

class BlockHardLimeStainedGlass(blockstate: BlockState?) : Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_LIME_STAINED_GLASS)
            get() = Companion.field
    }
}