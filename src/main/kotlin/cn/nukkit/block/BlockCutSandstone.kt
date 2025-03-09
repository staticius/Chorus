package cn.nukkit.block

class BlockCutSandstone @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSandstone(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(CUT_SANDSTONE)
            get() = Companion.field
    }
}
