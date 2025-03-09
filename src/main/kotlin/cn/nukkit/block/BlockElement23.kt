package cn.nukkit.block

class BlockElement23 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_23")
            get() = Companion.field
    }
}