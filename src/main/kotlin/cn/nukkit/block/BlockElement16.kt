package cn.nukkit.block

class BlockElement16 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_16")
            get() = Companion.field
    }
}