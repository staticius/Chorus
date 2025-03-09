package cn.nukkit.block

class BlockElement36 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_36")
            get() = Companion.field
    }
}