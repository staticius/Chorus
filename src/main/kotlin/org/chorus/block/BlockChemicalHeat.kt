package org.chorus.block

class BlockChemicalHeat @JvmOverloads constructor(blockState: BlockState = Companion.properties.defaultState) :
    Block(blockState) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CHEMICAL_HEAT)
    }
}