package cn.nukkit.block

class BlockElement58 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_58")
            get() = Companion.field
    }
}