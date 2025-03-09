package cn.nukkit.block

class BlockElement72 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_72")
            get() = Companion.field
    }
}