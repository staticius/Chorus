package cn.nukkit.block

class BlockBirchPlanks @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockPlanks(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(BIRCH_PLANKS)
            get() = Companion.field
    }
}