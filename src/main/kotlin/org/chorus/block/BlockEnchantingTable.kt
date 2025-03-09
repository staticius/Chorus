package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.blockentity.*
import cn.nukkit.item.*
import cn.nukkit.math.BlockFace
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.StringTag
import cn.nukkit.nbt.tag.Tag

/**
 * @author CreeperFace
 * @since 2015/11/22
 */
class BlockEnchantingTable @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockTransparent(blockstate), BlockEntityHolder<BlockEntityEnchantTable> {
    override val name: String
        get() = "Enchanting Table"

    override val blockEntityType: String
        get() = BlockEntity.ENCHANT_TABLE

    override val blockEntityClass: Class<out E>
        get() = BlockEntityEnchantTable::class.java

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val hardness: Double
        get() = 5.0

    override val resistance: Double
        get() = 6000.0

    override val waterloggingLevel: Int
        get() = 1

    override val lightLevel: Int
        get() = 7

    override fun canBeActivated(): Boolean {
        return true
    }

    override var maxY: Double
        get() = y + 12 / 16.0
        set(maxY) {
            super.maxY = maxY
        }

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

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
        val nbt = CompoundTag()

        if (item.hasCustomName()) {
            nbt.putString("CustomName", item.customName)
        }

        if (item.hasCustomBlockData()) {
            val customData: Map<String?, Tag?> = item.customBlockData!!.getTags()
            for ((key, value) in customData) {
                nbt.put(key, value)
            }
        }

        return BlockEntityHolder.Companion.setBlockAndCreateEntity<BlockEntityEnchantTable?, BlockEnchantingTable>(
            this,
            true,
            true,
            nbt
        ) != null
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (player == null) {
            return true
        }
        val itemInHand = player.getInventory().itemInHand
        if (player.isSneaking() && !(itemInHand.isTool || itemInHand.isNull)) {
            return false
        }

        val enchantTable = getOrCreateBlockEntity()
        if (enchantTable.namedTag.contains("Lock") && enchantTable.namedTag.get("Lock") is StringTag
            && (enchantTable.namedTag.getString("Lock") != item.customName)
        ) {
            return false
        }

        player.addWindow(enchantTable.getInventory())

        return true
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override fun isSolid(side: BlockFace): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(ENCHANTING_TABLE)
            get() = Companion.field
    }
}
