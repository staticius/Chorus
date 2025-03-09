package cn.nukkit.block

/**
 * @author MagicDroidX (Nukkit Project)
 */
abstract class BlockTransparent(blockState: BlockState?) : Block(blockState) {
    override val isTransparent: Boolean
        get() = true
}
