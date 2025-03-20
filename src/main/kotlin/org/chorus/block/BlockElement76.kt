package org.chorus.block

class BlockElement76 @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    Block(blockstate,) {
        
    override val properties: BlockProperties
        get() = Companion.properties
    
    companion object {
        val properties: BlockProperties = BlockProperties("minecraft:element_76")
    }
}