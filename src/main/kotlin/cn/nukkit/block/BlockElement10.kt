package cn.nukkit.block

class BlockElement10 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_10")
            get() = Companion.field
    }
}