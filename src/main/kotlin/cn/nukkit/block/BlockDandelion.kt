package cn.nukkit.block

class BlockDandelion @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFlower(blockstate) {
    override fun getUncommonFlower(): Block {
        return get(RED_TULIP)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(DANDELION)
            get() = Companion.field
    }
}