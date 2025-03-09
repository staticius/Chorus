package cn.nukkit.block

class BlockElement0 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_0")
            get() = Companion.field
    }
}