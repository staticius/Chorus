package org.chorus_oss.chorus.block

class BlockPoppy : BlockFlower {
    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.POPPY)
    }
}