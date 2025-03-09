package cn.nukkit.block

class BlockElement30 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_30")
            get() = Companion.field
    }
}