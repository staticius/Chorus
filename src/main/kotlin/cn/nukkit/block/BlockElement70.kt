package cn.nukkit.block

class BlockElement70 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_70")
            get() = Companion.field
    }
}