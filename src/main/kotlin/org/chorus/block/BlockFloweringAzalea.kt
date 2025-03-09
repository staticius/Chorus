package org.chorus.block

class BlockFloweringAzalea @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockAzalea(blockstate) {
    override val name: String
        get() = "FloweringAzalea"

    companion object {
        val properties: BlockProperties = BlockProperties(FLOWERING_AZALEA)
            get() = Companion.field
    }
}
