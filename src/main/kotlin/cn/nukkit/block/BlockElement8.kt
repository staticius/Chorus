package cn.nukkit.block

class BlockElement8 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_8")
            get() = Companion.field
    }
}