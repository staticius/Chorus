package org.chorus.block

class BlockHardGreenStainedGlass(blockstate: BlockState?) : Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_GREEN_STAINED_GLASS)
            get() = Companion.field
    }
}