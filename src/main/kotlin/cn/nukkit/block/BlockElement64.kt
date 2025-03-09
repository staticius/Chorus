package cn.nukkit.block

class BlockElement64 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_64")
            get() = Companion.field
    }
}