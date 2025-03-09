package org.chorus.block

import org.chorus.utils.DyeColor

/**
 * @author CreeperFace
 * @since 7.8.2017
 */
abstract class BlockGlassStained(blockState: BlockState?) : BlockGlass(blockState) {
    abstract val dyeColor: DyeColor
}
