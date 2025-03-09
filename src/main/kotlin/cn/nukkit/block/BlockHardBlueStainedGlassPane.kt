package cn.nukkit.block

class BlockHardBlueStainedGlassPane(blockstate: BlockState?) : Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_BLUE_STAINED_GLASS_PANE)
            get() = Companion.field
    }
}