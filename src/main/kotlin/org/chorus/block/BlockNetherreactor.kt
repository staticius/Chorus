package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.blockentity.BlockEntity
import cn.nukkit.item.Item
import cn.nukkit.item.Item.Companion.get
import cn.nukkit.item.ItemID
import cn.nukkit.item.ItemTool
import cn.nukkit.math.BlockFace

/**
 * @author good777LUCKY
 */
class BlockNetherreactor @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockstate), BlockEntityHolder<BlockEntityNetherReactor?> {
    override val blockEntityType: String
        get() = BlockEntity.NETHER_REACTOR

    override val blockEntityClass: Class<out Any>
        get() = BlockEntityNetherReactor::class.java

    override val name: String
        get() = "Nether Reactor Core"

    override val hardness: Double
        get() = 10.0

    override val resistance: Double
        get() = 6.0

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun getDrops(item: Item): Array<Item?>? {
        return if (item.isPickaxe) {
            arrayOf(
                get(ItemID.DIAMOND, 0, 3),
                get(ItemID.IRON_INGOT, 0, 6)
            )
        } else {
            Item.EMPTY_ARRAY
        }
    }

    override fun place(
        item: Item,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player?
    ): Boolean {
        return BlockEntityHolder.setBlockAndCreateEntity(this) != null
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.NETHERREACTOR)
            get() = Companion.field
    }
}
