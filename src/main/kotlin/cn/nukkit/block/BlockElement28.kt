package cn.nukkit.block

class BlockElement28 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_28")
            get() = Companion.field
    }
}