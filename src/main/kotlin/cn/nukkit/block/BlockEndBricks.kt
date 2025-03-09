package cn.nukkit.block

class BlockEndBricks @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockEndStone(blockstate) {
    override val name: String
        get() = "End Stone Bricks"

    companion object {
        val properties: BlockProperties = BlockProperties(END_BRICKS)
            get() = Companion.field
    }
}