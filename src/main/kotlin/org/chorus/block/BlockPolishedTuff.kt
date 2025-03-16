package org.chorus.block

class BlockPolishedTuff @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockTuff(blockstate) {
    override val name: String
        get() = "Polished Tuff"

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.POLISHED_TUFF)

    }
}