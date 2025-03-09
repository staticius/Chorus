package cn.nukkit.block

class BlockElement90 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_90")
            get() = Companion.field
    }
}