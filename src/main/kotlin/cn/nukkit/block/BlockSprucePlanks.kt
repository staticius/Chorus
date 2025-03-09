package cn.nukkit.block

class BlockSprucePlanks @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockPlanks(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SPRUCE_PLANKS)
            get() = Companion.field
    }
}