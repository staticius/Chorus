package org.chorus_oss.chorus.item

import org.chorus_oss.chorus.block.*

/**
 * Pay attention to mapping between sign and standing_sign blocks. Item is specified through this.block, and the block is specified through toItem.
 */
abstract class ItemSign protected constructor(blockState: BlockState)
    : Item(
        when (blockState) {
            BlockStandingSign.properties.defaultState -> ItemID.OAK_SIGN
            BlockDarkoakStandingSign.properties.defaultState -> ItemID.DARK_OAK_SIGN
            else -> blockState.identifier.replace("_standing_sign", "_sign")
        }
    ) {

    init {
        this.blockState = blockState
    }

    override val maxStackSize: Int
        get() = 16
}
