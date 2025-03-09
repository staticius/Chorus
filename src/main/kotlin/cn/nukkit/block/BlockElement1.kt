package cn.nukkit.block

class BlockElement1 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_1")
            get() = Companion.field
    }
}