package cn.nukkit.block

class BlockElement100 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_100")
            get() = Companion.field
    }
}