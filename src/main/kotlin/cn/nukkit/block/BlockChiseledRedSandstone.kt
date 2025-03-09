package cn.nukkit.block

class BlockChiseledRedSandstone @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSandstone(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(CHISELED_RED_SANDSTONE)
            get() = Companion.field
    }
}
