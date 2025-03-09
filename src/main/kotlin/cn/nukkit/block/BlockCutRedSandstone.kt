package cn.nukkit.block

class BlockCutRedSandstone @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSandstone(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(CUT_RED_SANDSTONE)
            get() = Companion.field
    }
}
