package cn.nukkit.block

class BlockElement104 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_104")
            get() = Companion.field
    }
}