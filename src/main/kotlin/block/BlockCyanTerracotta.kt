package org.chorus_oss.chorus.block

class BlockCyanTerracotta @JvmOverloads constructor(blockstate: BlockState = properties.defaultState) :
    BlockHardenedClay(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CYAN_TERRACOTTA)
    }
}