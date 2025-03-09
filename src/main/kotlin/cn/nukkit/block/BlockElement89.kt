package cn.nukkit.block

class BlockElement89 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_89")
            get() = Companion.field
    }
}