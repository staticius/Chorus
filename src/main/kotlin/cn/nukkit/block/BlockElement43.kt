package cn.nukkit.block

class BlockElement43 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_43")
            get() = Companion.field
    }
}