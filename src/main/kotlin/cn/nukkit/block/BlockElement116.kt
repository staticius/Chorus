package cn.nukkit.block

class BlockElement116 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_116")
            get() = Companion.field
    }
}