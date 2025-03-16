package org.chorus.block

import org.chorus.utils.DyeColor

abstract class BlockGlassPaneStained(blockState: BlockState) : BlockGlassPane(blockState) {
    abstract fun getDyeColor(): DyeColor
}
