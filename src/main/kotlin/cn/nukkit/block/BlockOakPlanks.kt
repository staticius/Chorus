package cn.nukkit.block

class BlockOakPlanks @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockPlanks(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.OAK_PLANKS)
            get() = Companion.field
    }
}