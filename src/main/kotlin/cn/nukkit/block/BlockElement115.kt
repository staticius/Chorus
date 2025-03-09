package cn.nukkit.block

class BlockElement115 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_115")
            get() = Companion.field
    }
}