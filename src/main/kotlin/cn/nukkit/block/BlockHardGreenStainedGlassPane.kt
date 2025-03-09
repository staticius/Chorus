package cn.nukkit.block

class BlockHardGreenStainedGlassPane(blockstate: BlockState?) : Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_GREEN_STAINED_GLASS_PANE)
            get() = Companion.field
    }
}