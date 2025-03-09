package cn.nukkit.block

class BlockElement98 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_98")
            get() = Companion.field
    }
}