package org.chorus_oss.chorus.block

class BlockRedSand : BlockSand {
    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.RED_SAND)
    }
}
