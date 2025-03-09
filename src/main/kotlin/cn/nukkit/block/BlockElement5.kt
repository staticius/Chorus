package cn.nukkit.block

class BlockElement5 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_5")
            get() = Companion.field
    }
}