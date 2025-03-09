package org.chorus.block

class BlockHardOrangeStainedGlassPane(blockstate: BlockState?) : Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_ORANGE_STAINED_GLASS_PANE)
            get() = Companion.field
    }
}