package cn.nukkit.block

class BlockElement56 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_56")
            get() = Companion.field
    }
}