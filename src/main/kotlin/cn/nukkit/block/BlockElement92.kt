package cn.nukkit.block

class BlockElement92 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_92")
            get() = Companion.field
    }
}