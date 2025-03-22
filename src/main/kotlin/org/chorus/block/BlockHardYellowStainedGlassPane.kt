package org.chorus.block

class BlockHardYellowStainedGlassPane(blockstate: BlockState) : Block(blockstate) {

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.HARD_YELLOW_STAINED_GLASS_PANE)
    }
}