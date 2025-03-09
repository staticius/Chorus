package cn.nukkit.block

class BlockHardLimeStainedGlassPane(blockstate: BlockState?) : Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_LIME_STAINED_GLASS_PANE)
            get() = Companion.field
    }
}