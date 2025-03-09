package cn.nukkit.block

class BlockElement83 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_83")
            get() = Companion.field
    }
}