package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.OxidizationLevel

open class BlockDoubleCutCopperSlab @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockDoubleSlabCopperBase(blockstate) {
    override val slabName: String
        get() {
            val sb = StringBuilder(30)
            if (isWaxed) {
                sb.append("Waxed ")
            }
            val oxidizationLevel = oxidizationLevel
            if (OxidizationLevel.UNAFFECTED != oxidizationLevel) {
                val name = oxidizationLevel.name
                sb.append(name[0]).append(name.substring(1).lowercase()).append(' ')
            }
            return sb.append("Cut Copper").toString()
        }

    override val singleSlab: BlockState?
        get() = BlockCutCopperSlab.properties.defaultState

    override fun getCopperId(waxed: Boolean, oxidizationLevel: OxidizationLevel?): String {
        if (oxidizationLevel == null) {
            return id
        }
        return when (oxidizationLevel) {
            OxidizationLevel.UNAFFECTED -> if (waxed) WAXED_DOUBLE_CUT_COPPER_SLAB else DOUBLE_CUT_COPPER_SLAB
            OxidizationLevel.EXPOSED -> if (waxed) WAXED_EXPOSED_DOUBLE_CUT_COPPER_SLAB else EXPOSED_DOUBLE_CUT_COPPER_SLAB
            OxidizationLevel.WEATHERED -> if (waxed) WAXED_WEATHERED_DOUBLE_CUT_COPPER_SLAB else WEATHERED_DOUBLE_CUT_COPPER_SLAB
            OxidizationLevel.OXIDIZED -> if (waxed) WAXED_OXIDIZED_DOUBLE_CUT_COPPER_SLAB else OXIDIZED_DOUBLE_CUT_COPPER_SLAB
        }
    }

    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.UNAFFECTED
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(DOUBLE_CUT_COPPER_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}