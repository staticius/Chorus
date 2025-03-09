package cn.nukkit.block

class BlockElement40 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_40")
            get() = Companion.field
    }
}