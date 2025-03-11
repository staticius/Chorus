package org.chorus.block

import org.chorus.utils.DyeColor

/**
 * @author CreeperFace
 * @since 7.8.2017
 */
abstract class BlockGlassPaneStained(blockState: BlockState?) : BlockGlassPane(blockState) {
    abstract fun getDyeColor(): DyeColor
}
