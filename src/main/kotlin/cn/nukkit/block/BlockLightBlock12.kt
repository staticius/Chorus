package cn.nukkit.block

class BlockLightBlock12 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockLightBlock0(blockstate) {
    override val lightLevel: Int
        get() = 12

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_BLOCK_12)
            get() = Companion.field
    }
}