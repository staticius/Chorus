package cn.nukkit.block

class BlockElement54 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_54")
            get() = Companion.field
    }
}