package org.chorus_oss.chorus.experimental.block.definitions

import org.chorus_oss.chorus.experimental.block.BlockDefinition
import org.chorus_oss.chorus.experimental.block.state.CommonStates

object ChiseledBookshelf : BlockDefinition(
    identifier = "minecraft:chiseled_bookshelf",
    states = listOf(
        CommonStates.booksStored,
        CommonStates.direction
    )
)
