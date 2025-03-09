package cn.nukkit.block

class BlockElement79 @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_79")
            get() = Companion.field
    }
}