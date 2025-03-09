package org.chorus.block

class BlockHardPinkStainedGlassPane(blockstate: BlockState?) : Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_PINK_STAINED_GLASS_PANE)
            get() = Companion.field
    }
}