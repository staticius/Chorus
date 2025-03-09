package cn.nukkit.block

class BlockElement112 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_112")
            get() = Companion.field
    }
}