package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.Server.Companion.instance
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType
import cn.nukkit.block.property.type.BooleanPropertyType
import cn.nukkit.event.block.LeavesDecayEvent
import cn.nukkit.item.Item
import cn.nukkit.item.ItemID
import cn.nukkit.item.ItemTool
import cn.nukkit.item.enchantment.Enchantment
import cn.nukkit.level.Level
import cn.nukkit.math.BlockFace
import cn.nukkit.utils.Hash.hashBlock
import it.unimi.dsi.fastutil.longs.Long2LongMap
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap
import java.util.concurrent.ThreadLocalRandom

/**
 * @author Angelic47 (Nukkit Project)
 */
abstract class BlockLeaves(blockState: BlockState?) : BlockTransparent(blockState) {
    override val hardness: Double
        get() = 0.2

    override val toolType: Int
        get() = ItemTool.TYPE_HOE

    abstract val type: WoodType

    override val name: String
        get() = type.name + " Leaves"

    override val burnChance: Int
        get() = 30

    override val waterloggingLevel: Int
        get() = 1

    override val burnAbility: Int
        get() = 60

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
        this.isPersistent = true
        level.setBlock(this.position, this, true)
        return true
    }

    override fun getDrops(item: Item): Array<Item?>? {
        if (item.isShears) {
            return arrayOf(
                toItem()
            )
        }

        val drops: MutableList<Item> = ArrayList(1)
        val fortuneEnchantment = item.getEnchantment(Enchantment.ID_FORTUNE_DIGGING)

        val fortune = fortuneEnchantment?.level ?: 0
        val appleOdds: Int
        val stickOdds: Int
        val saplingOdds: Int
        when (fortune) {
            0 -> {
                appleOdds = 200
                stickOdds = 50
                saplingOdds = if (type == WoodType.JUNGLE) 40 else 20
            }

            1 -> {
                appleOdds = 180
                stickOdds = 45
                saplingOdds = if (type == WoodType.JUNGLE) 36 else 16
            }

            2 -> {
                appleOdds = 160
                stickOdds = 40
                saplingOdds = if (type == WoodType.JUNGLE) 32 else 12
            }

            else -> {
                appleOdds = 120
                stickOdds = 30
                saplingOdds = if (type == WoodType.JUNGLE) 24 else 10
            }
        }

        val random = ThreadLocalRandom.current()
        if (canDropApple() && random.nextInt(appleOdds) == 0) {
            drops.add(Item.get(ItemID.APPLE))
        }
        if (random.nextInt(stickOdds) == 0) {
            drops.add(Item.get(ItemID.STICK))
        }
        if (random.nextInt(saplingOdds) == 0) {
            drops.add(toSapling())
        }

        return drops.toArray(Item.EMPTY_ARRAY)
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_RANDOM) {
            if (isCheckDecay) {
                if (isPersistent || findLog(this, 7, null)) {
                    isCheckDecay = false
                    level.setBlock(this.position, this, false, false)
                } else {
                    val ev = LeavesDecayEvent(this)
                    instance!!.pluginManager.callEvent(ev)
                    if (!ev.isCancelled) {
                        level.useBreakOn(this.position)
                    }
                }
                return type
            }
        } else if (type == Level.BLOCK_UPDATE_NORMAL || type == Level.BLOCK_UPDATE_SCHEDULED) {
            if (!isCheckDecay) {
                isCheckDecay = true
                level.setBlock(this.position, this, false, false)
            }

            // Slowly propagates the need to update instead of peaking down the TPS for huge trees
            for (side in BlockFace.entries) {
                val other = getSide(side)
                if (other is BlockLeaves) {
                    if (!other.isCheckDecay) {
                        level.scheduleUpdate(other, 2)
                    }
                }
            }
            return type
        }
        return type
    }

    private fun findLog(current: Block?, distance: Int, visited: Long2LongMap?): Boolean {
        var visited = visited
        if (visited == null) {
            visited = Long2LongOpenHashMap()
            visited.defaultReturnValue(-1)
        }
        if (current is IBlockWood || current is BlockMangroveRoots) {
            return true
        }
        if (distance == 0 || current !is BlockLeaves) {
            return false
        }
        val hash = hashBlock(current.position)
        if (visited.get(hash) >= distance) {
            return false
        }
        visited.put(hash, distance.toLong())
        for (face in VISIT_ORDER) {
            if (findLog(current.getSide(face), distance - 1, visited)) {
                return true
            }
        }
        return false
    }

    var isCheckDecay: Boolean
        get() = getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.UPDATE_BIT)
        set(checkDecay) {
            setPropertyValue<Boolean, BooleanPropertyType>(
                CommonBlockProperties.UPDATE_BIT,
                checkDecay
            )
        }

    var isPersistent: Boolean
        get() = getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.PERSISTENT_BIT)
        set(persistent) {
            setPropertyValue<Boolean, BooleanPropertyType>(
                CommonBlockProperties.PERSISTENT_BIT,
                persistent
            )
        }

    override fun canSilkTouch(): Boolean {
        return true
    }

    protected fun canDropApple(): Boolean {
        return type == WoodType.OAK
    }

    override fun diffusesSkyLight(): Boolean {
        return true
    }

    override fun breaksWhenMoved(): Boolean {
        return true
    }

    override fun sticksToPiston(): Boolean {
        return false
    }

    open fun toSapling(): Item {
        return Item.AIR
    }

    companion object {
        private val VISIT_ORDER = arrayOf(
            BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.DOWN, BlockFace.UP
        )
    }
}
