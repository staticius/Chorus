package cn.nukkit.block

class BlockHardLightBlueStainedGlass(blockstate: BlockState?) : Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_LIGHT_BLUE_STAINED_GLASS)
            get() = Companion.field
    }
}