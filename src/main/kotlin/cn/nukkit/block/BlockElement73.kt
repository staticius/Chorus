package cn.nukkit.block

class BlockElement73 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_73")
            get() = Companion.field
    }
}