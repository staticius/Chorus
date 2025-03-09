package cn.nukkit.block

class BlockElement108 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_108")
            get() = Companion.field
    }
}