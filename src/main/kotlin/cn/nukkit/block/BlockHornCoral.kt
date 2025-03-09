package cn.nukkit.block

open class BlockHornCoral @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCoral(blockstate) {
    override val isDead: Boolean
        get() = false

    override val deadCoral: Block
        get() = BlockDeadHornCoral()

    companion object {
        val properties: BlockProperties = BlockProperties(HORN_CORAL)
            get() = Companion.field
    }
}