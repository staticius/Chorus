package cn.nukkit.block

class BlockHardCyanStainedGlass(blockstate: BlockState?) : Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_CYAN_STAINED_GLASS)
            get() = Companion.field
    }
}