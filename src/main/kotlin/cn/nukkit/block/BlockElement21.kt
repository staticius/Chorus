package cn.nukkit.block

class BlockElement21 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_21")
            get() = Companion.field
    }
}