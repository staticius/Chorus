package cn.nukkit.block

class BlockElement34 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_34")
            get() = Companion.field
    }
}