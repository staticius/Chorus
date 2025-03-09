package cn.nukkit.block

class BlockElement74 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_74")
            get() = Companion.field
    }
}