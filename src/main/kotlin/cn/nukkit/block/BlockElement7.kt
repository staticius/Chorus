package cn.nukkit.block

class BlockElement7 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_7")
            get() = Companion.field
    }
}