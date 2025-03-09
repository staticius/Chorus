package cn.nukkit.block

class BlockHardYellowStainedGlass(blockstate: BlockState?) : Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_YELLOW_STAINED_GLASS)
            get() = Companion.field
    }
}