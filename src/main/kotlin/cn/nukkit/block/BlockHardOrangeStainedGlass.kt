package cn.nukkit.block

class BlockHardOrangeStainedGlass(blockstate: BlockState?) : Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_ORANGE_STAINED_GLASS)
            get() = Companion.field
    }
}