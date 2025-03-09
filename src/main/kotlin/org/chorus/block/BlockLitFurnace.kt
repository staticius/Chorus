package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.CommonPropertyMap
import org.chorus.blockentity.*
import org.chorus.inventory.ContainerInventory.Companion.calculateRedstone
import org.chorus.item.*
import org.chorus.math.BlockFace
import org.chorus.math.BlockFace.Companion.fromHorizontalIndex
import org.chorus.nbt.tag.CompoundTag
import org.chorus.nbt.tag.ListTag
import org.chorus.nbt.tag.StringTag
import org.chorus.nbt.tag.Tag
import org.chorus.utils.Faceable

open class BlockLitFurnace @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockstate), Faceable, BlockEntityHolder<BlockEntityFurnace?> {
    override val name: String
        get() = "Burning Furnace"

    override val blockEntityClass: Class<out BlockEntityFurnace>
        get() = BlockEntityFurnace::class.java

    override val blockEntityType: String
        get() = BlockEntity.FURNACE

    override fun canBeActivated(): Boolean {
        return true
    }

    override val hardness: Double
        get() = 3.5

    override val resistance: Double
        get() = 17.5

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val lightLevel: Int
        get() = 13

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
        blockFace = if (player != null) fromHorizontalIndex(
            player.getDirection()!!.getOpposite()!!.horizontalIndex
        ) else BlockFace.SOUTH

        val nbt = CompoundTag().putList("Items", ListTag())

        if (item.hasCustomName()) {
            nbt!!.putString("CustomName", item.customName)
        }

        if (item.hasCustomBlockData()) {
            val customData: Map<String?, Tag?> = item.customBlockData!!.getTags()
            for ((key, value) in customData) {
                nbt!!.put(key, value)
            }
        }

        return BlockEntityHolder.setBlockAndCreateEntity(this, true, true, nbt) != null
    }

    override fun onBreak(item: Item?): Boolean {
        level.setBlock(this.position, get(BlockID.AIR), true, true)
        return true
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
            return false
        }
        val itemInHand = player.getInventory().itemInHand
        if (player.isSneaking() && !(itemInHand.isTool || itemInHand.isNull)) {
            return false
        }

        val furnace = orCreateBlockEntity!!
        if (furnace.namedTag.contains("Lock") && furnace.namedTag.get("Lock") is StringTag
            && (furnace.namedTag.getString("Lock") != item.customName)
        ) {
            return false
        }

        player.addWindow(furnace.getInventory())
        return true
    }

    override fun toItem(): Item? {
        return ItemBlock(get(BlockID.FURNACE))
    }

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun hasComparatorInputOverride(): Boolean {
        return true
    }

    override val comparatorInputOverride: Int
        get() {
            val blockEntity = blockEntity

            if (blockEntity != null) {
                return calculateRedstone(blockEntity.getInventory())
            }

            return super.comparatorInputOverride
        }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override var blockFace: BlockFace?
        get() = CommonPropertyMap.CARDINAL_BLOCKFACE[getPropertyValue(
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION
        )]
        set(face) {
            this.setPropertyValue(
                CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
                CommonPropertyMap.CARDINAL_BLOCKFACE.inverse()[face]
            )
        }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.LIT_FURNACE, CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION)
            get() = Companion.field
    }
}