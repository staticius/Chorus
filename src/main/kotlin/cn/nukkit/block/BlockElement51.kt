package cn.nukkit.block

class BlockElement51 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_51")
            get() = Companion.field
    }
}