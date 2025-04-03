package org.chorus.block

import org.chorus.Player
import org.chorus.block.Block.Companion.get
import org.chorus.block.property.CommonBlockProperties
import org.chorus.inventory.BlockInventoryHolder
import org.chorus.inventory.Inventory
import org.chorus.item.Item
import org.chorus.item.Item.Companion.get
import org.chorus.item.ItemBlock
import org.chorus.item.ItemTool
import org.chorus.math.BlockFace
import org.chorus.math.BlockFace.Companion.fromHorizontalIndex
import org.chorus.registry.BiomeRegistry.get
import org.chorus.registry.BlockRegistry.get
import java.util.function.Supplier

class BlockStonecutterBlock @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockTransparent(blockstate), Faceable, BlockInventoryHolder {
    override val name: String
        get() = "Stonecutter"

    var blockFace: BlockFace
        get() = CommonPropertyMap.CARDINAL_BLOCKFACE.get(
            getPropertyValue<MinecraftCardinalDirection, EnumPropertyType<MinecraftCardinalDirection>>(
                CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION
            )
        )
        set(face) {
            val horizontalIndex = face.horizontalIndex
            if (horizontalIndex > -1) {
                this.setPropertyValue<MinecraftCardinalDirection, EnumPropertyType<MinecraftCardinalDirection>>(
                    CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
                    CommonPropertyMap.CARDINAL_BLOCKFACE.inverse().get(fromHorizontalIndex(horizontalIndex))
                )
            }
        }

    override fun place(
        item: Item?,
        block: Block,
        target: Block?,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player?
    ): Boolean {
        blockFace =
            if (player != null) fromHorizontalIndex(player.getDirection().horizontalIndex) else BlockFace.SOUTH

        level.setBlock(block.position, this, true, true)
        return true
    }

    override fun canBeActivated(): Boolean {
        return true
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        player?.addWindow(inventory!!)
        return true
    }

    override val hardness: Double
        get() = 3.5

    override val resistance: Double
        get() = 17.5

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val waterloggingLevel: Int
        get() = 1

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun getDrops(item: Item): Array<Item> {
        return arrayOf(toItem())
    }

    override fun toItem(): Item {
        return ItemBlock(BlockStonecutterBlock())
    }

    override var maxY: Double
        get() = position.y + 9 / 16.0
        set(maxY) {
            super.maxY = maxY
        }

    override fun blockInventorySupplier(): Supplier<Inventory?> {
        return Supplier<Inventory?> { StonecutterInventory(this) }
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STONECUTTER_BLOCK, CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION)

    }
}
