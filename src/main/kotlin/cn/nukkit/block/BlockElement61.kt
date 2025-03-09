package cn.nukkit.block

class BlockElement61 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_61")
            get() = Companion.field
    }
}