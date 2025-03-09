package cn.nukkit.block

class BlockLightBlock14 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockLightBlock0(blockstate) {
    override val lightLevel: Int
        get() = 14

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_BLOCK_14)
            get() = Companion.field
    }
}