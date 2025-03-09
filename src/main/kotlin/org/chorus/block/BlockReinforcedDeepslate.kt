package org.chorus.block


class BlockReinforcedDeepslate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockSolid(blockstate) {
    override val name: String
        get() = "ReinForced DeepSlate"

    override val resistance: Double
        get() = 1200.0

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.REINFORCED_DEEPSLATE)
            get() = Companion.field
    }
}
