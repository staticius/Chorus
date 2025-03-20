package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.Item
import org.chorus.math.BlockFace

//todo complete
class BlockSnifferEgg : BlockTransparent {
    constructor() : super(Companion.properties.getDefaultState())

    constructor(blockstate: BlockState) : super(blockstate)

    override val name: String
        get() = "Sniffer Egg"

    override fun place(
        item: Item,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player?
    ): Boolean {
        this.setPropertyValue<CrackedState, EnumPropertyType<CrackedState>>(
            CommonBlockProperties.CRACKED_STATE,
            CrackedState.NO_CRACKS
        )
        return level.setBlock(this.position, this)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SNIFFER_EGG, CommonBlockProperties.CRACKED_STATE)

    }
}