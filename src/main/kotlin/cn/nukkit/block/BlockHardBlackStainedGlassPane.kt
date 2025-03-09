package cn.nukkit.block

class BlockHardBlackStainedGlassPane(blockstate: BlockState?) : Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_BLACK_STAINED_GLASS_PANE)
            get() = Companion.field
    }
}