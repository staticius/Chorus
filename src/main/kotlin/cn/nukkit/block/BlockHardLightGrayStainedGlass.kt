package cn.nukkit.block

class BlockHardLightGrayStainedGlass(blockstate: BlockState?) : Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_LIGHT_GRAY_STAINED_GLASS)
            get() = Companion.field
    }
}