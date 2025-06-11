package org.chorus_oss.chorus.block

class BlockOrangeTulip : BlockFlower {
    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.ORANGE_TULIP)
    }
}