package org.chorus.block

class BlockPackedMud : BlockSolid {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    override val name: String
        get() = "Packed Mud"

    override val hardness: Double
        get() = 1.0

    override val resistance: Double
        get() = 3.0

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PACKED_MUD)
            
    }
}
