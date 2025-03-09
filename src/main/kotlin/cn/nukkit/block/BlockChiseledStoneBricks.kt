package cn.nukkit.block

class BlockChiseledStoneBricks @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStoneBricks(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(CHISELED_STONE_BRICKS)
            get() = Companion.field
    }
}
