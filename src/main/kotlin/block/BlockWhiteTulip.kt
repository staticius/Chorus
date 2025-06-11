package org.chorus_oss.chorus.block

class BlockWhiteTulip : BlockFlower {
    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WHITE_TULIP)
    }
}