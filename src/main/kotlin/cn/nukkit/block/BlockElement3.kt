package cn.nukkit.block

class BlockElement3 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_3")
            get() = Companion.field
    }
}