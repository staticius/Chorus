package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.type.BooleanPropertyType
import cn.nukkit.block.property.type.IntPropertyType
import cn.nukkit.blockentity.*
import cn.nukkit.dispenser.DispenseBehavior
import cn.nukkit.dispenser.DispenseBehaviorRegister.getBehavior
import cn.nukkit.dispenser.DropperDispenseBehavior
import cn.nukkit.dispenser.FlintAndSteelDispenseBehavior
import cn.nukkit.inventory.ContainerInventory.Companion.calculateRedstone
import cn.nukkit.inventory.InventoryHolder
import cn.nukkit.item.*
import cn.nukkit.item.Item.isNull
import cn.nukkit.level.Level
import cn.nukkit.level.Sound
import cn.nukkit.math.*
import cn.nukkit.math.BlockFace.Companion.fromIndex
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.nbt.tag.Tag
import cn.nukkit.network.protocol.LevelEventPacket
import cn.nukkit.utils.Faceable
import cn.nukkit.utils.RedstoneComponent
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.abs

/**
 * @author CreeperFace
 * @since 15.4.2017
 */
open class BlockDispenser @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockstate), RedstoneComponent, Faceable, BlockEntityHolder<BlockEntityEjectable?> {
    override fun hasComparatorInputOverride(): Boolean {
        return true
    }

    override val name: String
        get() = "Dispenser"

    override fun getBlockEntityType(): String {
        return BlockEntity.DISPENSER
    }

    override val hardness: Double
        get() = 3.5

    override val resistance: Double
        get() = 3.5

    override fun getBlockEntityClass(): Class<out BlockEntityEjectable> {
        return BlockEntityDispenser::class.java
    }

    override fun toItem(): Item? {
        return ItemBlock(this, 0)
    }

    override val comparatorInputOverride: Int
        get() {
            val blockEntity: InventoryHolder? = blockEntity

            if (blockEntity != null) {
                return calculateRedstone(blockEntity.inventory)
            }

            return 0
        }

    var isTriggered: Boolean
        get() = getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.TRIGGERED_BIT)
        set(value) {
            setPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.TRIGGERED_BIT, value)
        }

    override fun canBeActivated(): Boolean {
        return true
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

        val blockEntity = blockEntity ?: return false

        player.addWindow(blockEntity.inventory!!)
        return true
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
        if (player != null) {
            if (abs(player.position.x - position.x) < 2 && abs(player.position.z - position.z) < 2) {
                val y = player.position.y + player.getEyeHeight()

                blockFace = if (y - position.y > 2) {
                    BlockFace.UP
                } else if (position.y - y > 0) {
                    BlockFace.DOWN
                } else {
                    player.getHorizontalFacing().getOpposite()
                }
            } else {
                blockFace = player.getHorizontalFacing().getOpposite()
            }
        }

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

    override fun onUpdate(type: Int): Int {
        if (!level.server.settings.levelSettings().enableRedstone()) {
            return 0
        }

        if (type == Level.BLOCK_UPDATE_SCHEDULED) {
            this.dispense()

            return type
        } else if (type == Level.BLOCK_UPDATE_REDSTONE || type == Level.BLOCK_UPDATE_NORMAL) {
            val triggered = this.isTriggered

            if (this.isGettingPower && !triggered) {
                this.isTriggered = true
                level.setBlock(this.position, this, false, false)
                level.scheduleUpdate(this, this.position, 4)
            } else if (!this.isGettingPower && triggered) {
                this.isTriggered = false
                level.setBlock(this.position, this, false, false)
            }

            return type
        }

        return 0
    }

    open fun dispense() {
        val blockEntity = blockEntity ?: return

        val rand: Random = ThreadLocalRandom.current()
        var r = 1
        var slot = -1
        var target: Item? = null

        val inv = blockEntity.inventory
        for ((key, item) in inv!!.contents.entrySet()) {
            if (!item.isNull && rand.nextInt(r++) == 0) {
                target = item
                slot = key
            }
        }

        val pk = LevelEventPacket()

        val facing = blockFace

        pk.x = 0.5f + facing!!.xOffset * 0.7f
        pk.y = 0.5f + facing.yOffset * 0.7f
        pk.z = 0.5f + facing.zOffset * 0.7f

        if (target == null) {
            level.addSound(this.position, Sound.RANDOM_CLICK, 1.0f, 1.2f)
            getBlockEntity()!!.setDirty()
            return
        } else {
            if (getDispenseBehavior(target) !is DropperDispenseBehavior && getDispenseBehavior(target) !is FlintAndSteelDispenseBehavior) level.addSound(
                this.position, Sound.RANDOM_CLICK, 1.0f, 1.0f
            )
        }

        pk.evid = LevelEventPacket.EVENT_PARTICLE_SHOOT
        pk.data = 7
        level.addChunkPacket(position.chunkX, position.chunkZ, pk)

        val origin: Item = target
        target = target.clone()

        val behavior = getDispenseBehavior(target)
        val result = behavior.dispense(this, facing, target)

        target.count--
        inv.setItem(slot, target)

        if (result != null) {
            if (result.id == origin.id || result.damage != origin.damage) {
                val fit = inv.addItem(result)

                if (fit.size > 0) {
                    for (drop in fit) {
                        level.dropItem(this.position, drop)
                    }
                }
            } else {
                inv.setItem(slot, result)
            }
        }
    }

    protected open fun getDispenseBehavior(item: Item): DispenseBehavior {
        return getBehavior(item.id)
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    val dispensePosition: Vector3?
        get() {
            val facing = blockFace
            return position.add(
                0.5 + 0.7 * facing!!.xOffset,
                0.5 + 0.7 * facing.yOffset,
                0.5 + 0.7 * facing.zOffset
            )
        }

    override var blockFace: BlockFace?
        get() = fromIndex(getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.FACING_DIRECTION))
        set(face) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.FACING_DIRECTION, face!!.index)
        }

    companion object {
        val properties: BlockProperties =
            BlockProperties(DISPENSER, CommonBlockProperties.FACING_DIRECTION, CommonBlockProperties.TRIGGERED_BIT)
            get() = Companion.field
    }
}
