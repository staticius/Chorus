package cn.nukkit.block

class BlockDeadHornCoral @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockHornCoral(blockstate) {
    override val isDead: Boolean
        get() = true

    companion object {
        val properties: BlockProperties = BlockProperties(DEAD_HORN_CORAL)
            get() = Companion.field
    }
}