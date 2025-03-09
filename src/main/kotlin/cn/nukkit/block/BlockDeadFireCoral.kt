package cn.nukkit.block

class BlockDeadFireCoral @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFireCoral(blockstate) {
    override val isDead: Boolean
        get() = true

    companion object {
        val properties: BlockProperties = BlockProperties(DEAD_FIRE_CORAL)
            get() = Companion.field
    }
}