package cn.nukkit.block

class BlockElement69 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_69")
            get() = Companion.field
    }
}