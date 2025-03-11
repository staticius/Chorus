package org.chorus.block

class BlockChemicalHeat @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    Block(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CHEMICAL_HEAT)

    }
}