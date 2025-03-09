package cn.nukkit.block

class BlockElement37 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_37")
            get() = Companion.field
    }
}