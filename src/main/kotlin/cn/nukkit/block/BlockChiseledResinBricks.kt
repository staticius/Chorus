package cn.nukkit.block

class BlockChiseledResinBricks @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockResinBricks(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(CHISELED_RESIN_BRICKS)
            get() = Companion.field
    }
}
