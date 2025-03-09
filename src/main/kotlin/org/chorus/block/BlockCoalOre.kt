package org.chorus.block

import cn.nukkit.item.ItemID
import cn.nukkit.item.ItemTool
import java.util.concurrent.ThreadLocalRandom

open class BlockCoalOre @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockOre(blockstate) {
    override val name: String
        get() = "Coal Ore"

    override fun getRawMaterial(): String? {
        return ItemID.COAL
    }

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val dropExp: Int
        get() = ThreadLocalRandom.current().nextInt(3)

    override val resistance: Double
        get() = 3.0

    override val hardness: Double
        get() = 3.0

    companion object {
        val properties: BlockProperties = BlockProperties(COAL_ORE)
            get() = Companion.field
    }
}