package cn.nukkit.block

import cn.nukkit.block.Block.name
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.math.Vector3.equals

open class BlockCutCopperSlab : BlockSlabCopperBase {
    @JvmOverloads
    constructor(blockstate: BlockState? = Companion.properties.defaultState) : super(blockstate, DOUBLE_CUT_COPPER_SLAB)

    constructor(blockstate: BlockState?, doubleSlabId: String?) : super(blockstate, doubleSlabId)

    override fun getSlabName(): String {
        val sb = StringBuilder(30)
        if (isWaxed) {
            sb.append("Waxed ")
        }
        val oxidizationLevel: OxidizationLevel = oxidizationLevel
        if (OxidizationLevel.UNAFFECTED != oxidizationLevel) {
            val name: String = oxidizationLevel.name
            sb.append(name[0]).append(name.substring(1).lowercase()).append(' ')
        }
        return sb.append("Cut Copper").toString()
    }

    override fun getCopperId(waxed: Boolean, oxidizationLevel: OxidizationLevel?): String {
        if (oxidizationLevel == null) {
            return id
        }
        return when (oxidizationLevel) {
            OxidizationLevel.UNAFFECTED -> if (waxed) WAXED_CUT_COPPER_SLAB else CUT_COPPER_SLAB
            OxidizationLevel.EXPOSED -> if (waxed) WAXED_EXPOSED_CUT_COPPER_SLAB else EXPOSED_CUT_COPPER_SLAB
            OxidizationLevel.WEATHERED -> if (waxed) WAXED_WEATHERED_CUT_COPPER_SLAB else WEATHERED_CUT_COPPER_SLAB
            OxidizationLevel.OXIDIZED -> if (waxed) WAXED_OXIDIZED_CUT_COPPER_SLAB else OXIDIZED_CUT_COPPER_SLAB
        }
    }

    override fun getOxidizationLevel(): OxidizationLevel {
        return OxidizationLevel.UNAFFECTED
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(CUT_COPPER_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF)
            get() = Companion.field
    }
}