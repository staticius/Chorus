package cn.nukkit.block

class BlockElement50 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_50")
            get() = Companion.field
    }
}