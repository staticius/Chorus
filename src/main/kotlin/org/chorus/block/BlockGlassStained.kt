package org.chorus.block

import org.chorus.utils.DyeColor

abstract class BlockGlassStained(blockState: BlockState) : BlockGlass(blockState) {
    abstract fun getDyeColor(): DyeColor
}
