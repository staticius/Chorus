package cn.nukkit.block

import cn.nukkit.block.Block.name
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.math.Vector3.equals

open class BlockCutCopperStairs @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockStairsCopperBase(blockstate) {
    override val name: String
        get() {
            val sb = StringBuilder(30)
            if (isWaxed) {
                sb.append("Waxed ")
            }
            val oxidizationLevel: OxidizationLevel = oxidizationLevel
            if (OxidizationLevel.UNAFFECTED != oxidizationLevel) {
                val name: String = oxidizationLevel.name
                sb.append(name[0]).append(name.substring(1).lowercase()).append(' ')
            }
            return sb.append("Cut Copper Stairs").toString()
        }

    override fun getCopperId(waxed: Boolean, oxidizationLevel: OxidizationLevel?): String {
        if (oxidizationLevel == null) {
            return id
        }
        return when (oxidizationLevel) {
            OxidizationLevel.UNAFFECTED -> if (waxed) WAXED_CUT_COPPER_STAIRS else CUT_COPPER_STAIRS
            OxidizationLevel.EXPOSED -> if (waxed) WAXED_EXPOSED_CUT_COPPER_STAIRS else EXPOSED_CUT_COPPER_STAIRS
            OxidizationLevel.WEATHERED -> if (waxed) WAXED_WEATHERED_CUT_COPPER_STAIRS else WEATHERED_CUT_COPPER_STAIRS
            OxidizationLevel.OXIDIZED -> if (waxed) WAXED_OXIDIZED_CUT_COPPER_STAIRS else OXIDIZED_CUT_COPPER_STAIRS
        }
    }

    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.UNAFFECTED
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            CUT_COPPER_STAIRS,
            CommonBlockProperties.UPSIDE_DOWN_BIT,
            CommonBlockProperties.WEIRDO_DIRECTION
        )
            get() = Companion.field
    }
}