package cn.nukkit.block

import cn.nukkit.utils.DyeColor

/**
 * @author CreeperFace
 * @since 7.8.2017
 */
abstract class BlockGlassPaneStained(blockState: BlockState?) : BlockGlassPane(blockState) {
    abstract val dyeColor: DyeColor
}
