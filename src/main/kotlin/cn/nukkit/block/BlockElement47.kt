package cn.nukkit.block

class BlockElement47 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_47")
            get() = Companion.field
    }
}