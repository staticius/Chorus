package cn.nukkit.block

class BlockElement15 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_15")
            get() = Companion.field
    }
}