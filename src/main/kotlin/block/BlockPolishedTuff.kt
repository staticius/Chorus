package org.chorus_oss.chorus.block

class BlockPolishedTuff @JvmOverloads constructor(blockstate: BlockState = properties.defaultState) :
    BlockTuff(blockstate) {
    override val name: String
        get() = "Polished Tuff"

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.POLISHED_TUFF)
    }
}