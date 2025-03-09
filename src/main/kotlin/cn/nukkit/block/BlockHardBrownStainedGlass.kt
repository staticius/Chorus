package cn.nukkit.block

class BlockHardBrownStainedGlass(blockstate: BlockState?) : Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_BROWN_STAINED_GLASS)
            get() = Companion.field
    }
}