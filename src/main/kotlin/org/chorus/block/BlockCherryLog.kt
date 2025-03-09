package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties

class BlockCherryLog : BlockLog {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    override val hardness: Double
        get() = 2.0

    override val resistance: Double
        get() = 10.0

    override val burnChance: Int
        get() = 5

    override val burnAbility: Int
        get() = 5

    override val name: String
        get() = "Cherry log"

    override fun getStrippedState(): BlockState {
        return BlockStrippedCherryLog.properties.defaultState
    }

    companion object {
        val properties: BlockProperties = BlockProperties(CHERRY_LOG, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}

