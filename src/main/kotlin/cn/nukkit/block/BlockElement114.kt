package cn.nukkit.block

class BlockElement114 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_114")
            get() = Companion.field
    }
}