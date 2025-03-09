package cn.nukkit.block

class BlockAcaciaPlanks @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockPlanks(blockstate) {
    companion object {
        val properties: BlockProperties = BlockProperties(ACACIA_PLANKS)
            get() = Companion.field
    }
}