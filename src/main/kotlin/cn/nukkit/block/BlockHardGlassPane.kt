package cn.nukkit.block

class BlockHardGlassPane @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_GLASS_PANE)
            get() = Companion.field
    }
}