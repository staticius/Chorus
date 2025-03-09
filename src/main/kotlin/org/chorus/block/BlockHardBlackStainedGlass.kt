package org.chorus.block

class BlockHardBlackStainedGlass(blockstate: BlockState?) : Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_BLACK_STAINED_GLASS)
            get() = Companion.field
    }
}