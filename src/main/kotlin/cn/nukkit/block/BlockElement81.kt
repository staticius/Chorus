package cn.nukkit.block

class BlockElement81 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_81")
            get() = Companion.field
    }
}