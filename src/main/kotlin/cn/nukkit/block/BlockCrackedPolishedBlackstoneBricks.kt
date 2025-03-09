package cn.nukkit.block

class BlockCrackedPolishedBlackstoneBricks @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockBlackstone(blockstate) {
    override val name: String
        get() = "Cracked Polished Blackstone Bricks"

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(CRACKED_POLISHED_BLACKSTONE_BRICKS)
            get() = Companion.field
    }
}