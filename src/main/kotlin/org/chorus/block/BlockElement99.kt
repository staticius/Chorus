package org.chorus.block

class BlockElement99 @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    Block(blockstate,) {
        
    override val properties: BlockProperties
        get() = Companion.properties
    
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_99")
    }
}