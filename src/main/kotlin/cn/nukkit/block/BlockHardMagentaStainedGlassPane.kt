package cn.nukkit.block

class BlockHardMagentaStainedGlassPane(blockstate: BlockState?) : Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_MAGENTA_STAINED_GLASS_PANE)
            get() = Companion.field
    }
}