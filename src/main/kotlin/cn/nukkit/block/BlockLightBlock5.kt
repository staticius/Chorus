package cn.nukkit.block

class BlockLightBlock5 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockLightBlock0(blockstate) {
    override val lightLevel: Int
        get() = 5

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.LIGHT_BLOCK_5)
            get() = Companion.field
    }
}