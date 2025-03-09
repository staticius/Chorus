package cn.nukkit.block

class BlockElement39 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_39")
            get() = Companion.field
    }
}