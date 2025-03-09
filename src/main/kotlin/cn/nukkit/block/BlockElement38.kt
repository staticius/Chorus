package cn.nukkit.block

class BlockElement38 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_38")
            get() = Companion.field
    }
}