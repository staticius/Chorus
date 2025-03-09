package cn.nukkit.block

class BlockHardLightBlueStainedGlassPane(blockstate: BlockState?) : Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_LIGHT_BLUE_STAINED_GLASS_PANE)
            get() = Companion.field
    }
}