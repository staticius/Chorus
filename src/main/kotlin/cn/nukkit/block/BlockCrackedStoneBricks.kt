package cn.nukkit.block

class BlockCrackedStoneBricks @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStoneBricks(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(CRACKED_STONE_BRICKS)
            get() = Companion.field
    }
}
