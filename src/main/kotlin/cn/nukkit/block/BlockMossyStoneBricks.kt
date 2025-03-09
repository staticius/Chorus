package cn.nukkit.block

class BlockMossyStoneBricks @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStoneBricks(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MOSSY_STONE_BRICKS)
            get() = Companion.field
    }
}
