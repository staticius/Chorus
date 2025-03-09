package org.chorus.block

class BlockRawIronBlock @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    Block(blockstate) {
    override val name: String
        get() = "Block of Raw Iron"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.RAW_IRON_BLOCK)
            get() = Companion.field
    }
}