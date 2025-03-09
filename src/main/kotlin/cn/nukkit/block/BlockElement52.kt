package cn.nukkit.block

class BlockElement52 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_52")
            get() = Companion.field
    }
}