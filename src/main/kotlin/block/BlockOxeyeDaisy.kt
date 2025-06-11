package org.chorus_oss.chorus.block

class BlockOxeyeDaisy : BlockFlower {
    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.OXEYE_DAISY)
    }
}