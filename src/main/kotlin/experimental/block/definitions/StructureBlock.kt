package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object StructureBlock :
    BlockDefinition(
        identifier = "minecraft:structure_block",
        states = listOf(CommonStates.structureBlockType)
    )
