package org.chorus.block

import org.chorus.math.Vector3.equals

open class BlockCutCopper @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockCopperBase(blockstate) {
    override val name: String
        get() = "Cut Copper"

    override val oxidizationLevel: OxidizationLevel 
        get() = OxidizationLevel.UNAFFECTED
    

    override fun getCopperId(waxed: Boolean, oxidizationLevel: OxidizationLevel?): String {
        if (oxidizationLevel == null) {
            return id
        }
        return when (oxidizationLevel) {
            OxidizationLevel.UNAFFECTED -> if (waxed) WAXED_CUT_COPPER else CUT_COPPER
            OxidizationLevel.EXPOSED -> if (waxed) WAXED_EXPOSED_CUT_COPPER else EXPOSED_CUT_COPPER
            OxidizationLevel.WEATHERED -> if (waxed) WAXED_WEATHERED_CUT_COPPER else WEATHERED_CUT_COPPER
            OxidizationLevel.OXIDIZED -> if (waxed) WAXED_OXIDIZED_CUT_COPPER else OXIDIZED_CUT_COPPER
        }
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CUT_COPPER)

    }
}