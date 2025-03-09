package cn.nukkit.block

class BlockElement86 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_86")
            get() = Companion.field
    }
}