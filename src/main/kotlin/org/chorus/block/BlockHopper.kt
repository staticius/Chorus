package org.chorus.block

import org.chorus.Player
import org.chorus.Server.Companion.instance
import org.chorus.block.BlockComposter.isFull
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.BooleanPropertyType
import org.chorus.block.property.type.IntPropertyType
import org.chorus.blockentity.BlockEntity
import org.chorus.blockentity.BlockEntityHopper
import org.chorus.entity.item.EntityItem
import org.chorus.event.inventory.InventoryMoveItemEvent
import org.chorus.inventory.ContainerInventory.Companion.calculateRedstone
import org.chorus.inventory.InventoryHolder
import org.chorus.inventory.RecipeInventoryHolder
import org.chorus.item.Item
import org.chorus.item.ItemTool
import org.chorus.level.Level
import org.chorus.level.Locator
import org.chorus.math.*
import org.chorus.math.BlockFace.Companion.fromIndex
import org.chorus.nbt.tag.CompoundTag
import org.chorus.nbt.tag.ListTag
import org.chorus.utils.Faceable
import org.chorus.utils.RedstoneComponent

class BlockHopper @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockTransparent(blockstate), RedstoneComponent, Faceable, BlockEntityHolder<BlockEntityHopper?> {
    override val blockEntityClass: Class<out BlockEntityHopper>
        get() = BlockEntityHopper::class.java

    override val blockEntityType: String
        get() = BlockEntity.HOPPER

    override val name: String
        get() = "Hopper Block"

    override val hardness: Double
        get() = 3.0

    override val resistance: Double
        get() = 24.0

    override val waterloggingLevel: Int
        get() = 1

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
        var facing = face.getOpposite()

        if (facing == BlockFace.UP) {
            facing = BlockFace.DOWN
        }

        blockFace = facing

        if (Server.instance.settings.levelSettings().enableRedstone()) {
            val powered = this.isGettingPower

            if (powered == this.isEnabled) {
                this.isEnabled = !powered
            }
        }

        val nbt = CompoundTag().putList("Items", ListTag())
        return BlockEntityHolder.setBlockAndCreateEntity(this, true, true, nbt) != null
    }

    override fun onActivate(
        item: Item,
        player: Player,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (isNotActivate(player)) return false

        val blockEntity = getOrCreateBlockEntity()!!

        return player.addWindow(blockEntity.getInventory()) != -1
    }

    override fun canBeActivated(): Boolean {
        return true
    }

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

    var isEnabled: Boolean
        get() = !getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.TOGGLE_BIT)
        set(enabled) {
            setPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.TOGGLE_BIT, !enabled)
        }

    override fun onUpdate(type: Int): Int {
        if (!Server.instance.settings.levelSettings().enableRedstone()) {
            return 0
        }

        if (type == Level.BLOCK_UPDATE_NORMAL || type == Level.BLOCK_UPDATE_REDSTONE) {
            val disabled = level.isBlockPowered(this.position)

            if (disabled == this.isEnabled) {
                this.isEnabled = !disabled
                level.setBlock(this.position, this, false, true)
                val be = blockEntity
                if (be != null) {
                    be.isDisabled = disabled
                    if (!disabled) {
                        be.scheduleUpdate()
                    }
                }
            }

            return type
        }

        return 0
    }

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override var blockFace: BlockFace?
        get() = fromIndex(getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.FACING_DIRECTION))
        set(face) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.FACING_DIRECTION, face!!.index)
        }

    override fun isSolid(side: BlockFace): Boolean {
        return side == BlockFace.UP
    }

    interface IHopper {
        fun getLocator(): Locator?

        fun pullItems(hopperHolder: InventoryHolder, hopperPos: Locator): Boolean {
            val hopperInv: Unit = hopperHolder.inventory

            if (hopperInv.isFull) return false

            val blockSide = hopperPos.getSide(BlockFace.UP)!!.tickCachedLevelBlock
            val blockEntity =
                hopperPos.level.getBlockEntity(Vector3().setComponentsAdding(hopperPos.position, BlockFace.UP))

            if (blockEntity is InventoryHolder) {
                val inv = if (blockEntity is RecipeInventoryHolder) holder.productView else blockEntity.inventory!!

                for (i in 0..<inv.size) {
                    val item = inv.getItem(i)

                    if (!item.isNull) {
                        val itemToAdd: Item = item.clone()
                        itemToAdd.count = 1

                        if (!hopperInv.canAddItem(itemToAdd)) continue

                        val ev = InventoryMoveItemEvent(
                            inv,
                            hopperInv,
                            hopperHolder,
                            itemToAdd,
                            InventoryMoveItemEvent.Action.SLOT_CHANGE
                        )
                        instance!!.pluginManager.callEvent(ev)

                        if (ev.isCancelled) continue

                        val items: Array<Item> = hopperInv.addItem(itemToAdd)

                        if (items.size >= 1) continue

                        item.count--

                        inv.setItem(i, item)
                        return true
                    }
                }
            } else if (blockSide is BlockComposter) {
                if (blockSide.isFull) {
                    //检查是否能输入
                    if (!hopperInv.canAddItem(blockSide.outPutItem)) return false

                    //Will call BlockComposterEmptyEvent
                    val item = blockSide.empty()

                    if (item == null || item.isNull) return false

                    val itemToAdd: Item = item.clone()
                    itemToAdd.count = 1

                    val items: Array<Item> = hopperInv.addItem(itemToAdd)

                    return items.size < 1
                }
            }
            return false
        }

        fun pickupItems(hopperHolder: InventoryHolder, hopperPos: Locator, pickupArea: AxisAlignedBB): Boolean {
            val hopperInv: Unit = hopperHolder.inventory

            if (hopperInv.isFull) return false

            var pickedUpItem = false

            for (entity in hopperPos.level.getCollidingEntities(pickupArea)) {
                if (entity.isClosed() || entity !is EntityItem) continue

                val item = entity.getItem()

                if (item!!.isNull || !hopperInv.canAddItem(item)) continue

                val originalCount = item.getCount()

                val ev = InventoryMoveItemEvent(
                    null, hopperInv, hopperHolder,
                    item, InventoryMoveItemEvent.Action.PICKUP
                )
                instance!!.pluginManager.callEvent(ev)

                if (ev.isCancelled) continue

                val items: Array<Item> = hopperInv.addItem(item)

                if (items.size == 0) {
                    entity.close()
                    pickedUpItem = true
                    continue
                }

                if (items[0].getCount() != originalCount) {
                    pickedUpItem = true
                    item.setCount(items[0].getCount())
                }
            }

            return pickedUpItem
        }
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.HOPPER, CommonBlockProperties.FACING_DIRECTION, CommonBlockProperties.TOGGLE_BIT)

    }
}
