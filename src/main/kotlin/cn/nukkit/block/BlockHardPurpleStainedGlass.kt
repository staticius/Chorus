package cn.nukkit.block

class BlockHardPurpleStainedGlass(blockstate: BlockState?) : Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_PURPLE_STAINED_GLASS)
            get() = Companion.field
    }
}