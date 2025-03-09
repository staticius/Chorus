package cn.nukkit.block

class BlockElement27 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_27")
            get() = Companion.field
    }
}