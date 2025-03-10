package org.chorus.block


abstract class BlockTransparent(blockState: BlockState?) : Block(blockState) {
    override val isTransparent: Boolean
        get() = true
}
