package cn.nukkit.block

class BlockCrackedDeepslateTiles @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockDeepslateTiles(blockstate) {
    override val name: String
        get() = "Cracked Deepslate Tiles"

    companion object {
        val properties: BlockProperties = BlockProperties(CRACKED_DEEPSLATE_TILES)
            get() = Companion.field
    }
}