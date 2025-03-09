package cn.nukkit.block

class BlockHardRedStainedGlassPane(blockstate: BlockState?) : Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_RED_STAINED_GLASS_PANE)
            get() = Companion.field
    }
}