package cn.nukkit.block

class BlockElement13 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_13")
            get() = Companion.field
    }
}