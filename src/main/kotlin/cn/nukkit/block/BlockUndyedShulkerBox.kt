/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.blockentity.BlockEntity
import cn.nukkit.blockentity.BlockEntityShulkerBox
import cn.nukkit.inventory.ContainerInventory.Companion.calculateRedstone
import cn.nukkit.item.*
import cn.nukkit.math.BlockFace
import cn.nukkit.math.BlockFace.Companion.fromIndex
import cn.nukkit.nbt.NBTIO.putItemHelper
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.nbt.tag.Tag
import cn.nukkit.tags.BlockTags
import java.util.Set

/**
 * @author Reece Mackie
 */
open class BlockUndyedShulkerBox(blockState: BlockState?) : BlockTransparent(blockState),
    BlockEntityHolder<BlockEntityShulkerBox?> {
    override val blockEntityClass: Class<out BlockEntityShulkerBox>
        get() = BlockEntityShulkerBox::class.java

    override val blockEntityType: String
        get() = BlockEntity.SHULKER_BOX

    override val hardness: Double
        get() = 2.0

    override val resistance: Double
        get() = 10.0

    override fun canBeActivated(): Boolean {
        return true
    }

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val waterloggingLevel: Int
        get() = 1

    open val shulkerBox: Item
        get() = ItemBlock(this)

    override fun toItem(): Item? {
        val item = shulkerBox

        if (this.level == null) return item

        val tile = blockEntity ?: return item

        val inv = tile.realInventory

        if (!inv!!.isEmpty) {
            var nbt = item.namedTag
            if (nbt == null) {
                nbt = CompoundTag()
            }

            val items = ListTag<CompoundTag>()

            for (it in 0..<inv.size) {
                if (!inv.getItem(it).isNull) {
                    val d = putItemHelper(inv.getItem(it), it)
                    items.add(d)
                }
            }

            nbt.put("Items", items)

            item.setCompoundTag(nbt)
        }

        if (tile.hasName()) {
            item.setCustomName(tile.name)
        }

        return item
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
        val nbt = CompoundTag().putByte("facing", face.index)

        if (item.hasCustomName()) {
            nbt.putString("CustomName", item.customName)
        }

        val t = item.namedTag

        // This code gets executed when the player has broken the shulker box and placed it back (©Kevims 2020)
        if (t != null && t.contains("Items")) {
            nbt.putList("Items", t.getList("Items"))
        }

        // This code gets executed when the player has copied the shulker box in creative mode (©Kevims 2020)
        if (item.hasCustomBlockData()) {
            val customData: Map<String?, Tag?> = item.customBlockData!!.getTags()
            for ((key, value) in customData) {
                nbt.put(key, value)
            }
        }

        return BlockEntityHolder.setBlockAndCreateEntity(this, true, true, nbt) != null
    }

    override fun canHarvestWithHand(): Boolean {
        return false
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

        val box = orCreateBlockEntity!!
        val block = this.getSide(fromIndex(box.namedTag.getByte("facing").toInt())!!)
        if ((block !is BlockAir) && (block !is BlockLiquid) && (block !is BlockFlowable)) {
            return false
        }

        player.addWindow(box.getInventory())
        return true
    }

    override fun hasComparatorInputOverride(): Boolean {
        return true
    }

    override val comparatorInputOverride: Int
        get() {
            val be = blockEntity ?: return 0

            return calculateRedstone(be.getInventory())
        }

    override fun breaksWhenMoved(): Boolean {
        return true
    }

    override fun sticksToPiston(): Boolean {
        return false
    }

    override val isSolid: Boolean
        get() = false

    override fun isSolid(side: BlockFace): Boolean {
        return false
    }

    override val itemMaxStackSize: Int
        get() = 1

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.UNDYED_SHULKER_BOX, Set.of(BlockTags.PNX_SHULKERBOX))
            get() = Companion.field
    }
}
