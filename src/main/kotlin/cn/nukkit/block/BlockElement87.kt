package cn.nukkit.block

class BlockElement87 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_87")
            get() = Companion.field
    }
}