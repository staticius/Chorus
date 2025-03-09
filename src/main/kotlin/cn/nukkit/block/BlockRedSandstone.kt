package cn.nukkit.block

/**
 * @author CreeperFace
 * @since 26. 11. 2016
 */
class BlockRedSandstone @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockSandstone(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.RED_SANDSTONE)
            get() = Companion.field
    }
}
