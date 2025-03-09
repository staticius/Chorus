package cn.nukkit.block

class BlockElement110 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_110")
            get() = Companion.field
    }
}