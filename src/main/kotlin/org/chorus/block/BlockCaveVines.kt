package org.chorus.block

import org.chorus.Player
import org.chorus.Server.Companion.instance
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.IntPropertyType
import org.chorus.event.block.BlockGrowEvent
import org.chorus.item.*
import org.chorus.level.Level
import org.chorus.level.particle.BoneMealParticle
import org.chorus.math.BlockFace
import java.util.*
import java.util.concurrent.ThreadLocalRandom

open class BlockCaveVines @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockTransparent(blockstate) {
    override val name: String
        get() = "Cave Vines"

    override val hardness: Double
        get() = 0.0

    override val isTransparent: Boolean
        get() = true

    override val isSolid: Boolean
        get() = false

    override fun canBeActivated(): Boolean {
        return true
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (!isValidSupport(this)) {
                level.useBreakOn(this.position)
            }
            return type
        } else if (type == Level.BLOCK_UPDATE_RANDOM) {
            val random: Random = ThreadLocalRandom.current()
            //random mature,The feature that I added.
            if (random.nextInt(4) == 0) {
                val growth = growth
                if (growth + 4 < maxGrowth) {
                    val block = clone() as BlockCaveVines
                    block.growth = growth + 4
                    val ev = BlockGrowEvent(this, block)
                    instance!!.pluginManager.callEvent(ev)
                    if (!ev.isCancelled) {
                        level.setBlock(this.position, ev.newState!!, false, true)
                    } else {
                        return type
                    }
                } else {
                    val block = if (up() is BlockCaveVines && down() !is BlockCaveVines) {
                        BlockCaveVinesHeadWithBerries()
                    } else BlockCaveVinesBodyWithBerries()
                    block.growth = maxGrowth
                    val ev = BlockGrowEvent(this, block)
                    instance!!.pluginManager.callEvent(ev)
                    if (!ev.isCancelled) {
                        level.setBlock(this.position, ev.newState!!, false, true)
                    } else {
                        return type
                    }
                }
            }
            //random grow feature,according to wiki in https://minecraft.wiki/w/Glow_Berries#Growth
            if (down()!!.isAir && random.nextInt(10) == 0) {
                val block = if (up() is BlockCaveVines && down() !is BlockCaveVines) {
                    BlockCaveVinesHeadWithBerries()
                } else BlockCaveVinesBodyWithBerries()
                block.growth = maxGrowth
                val ev = BlockGrowEvent(this, block)
                instance!!.pluginManager.callEvent(ev)
                if (!ev.isCancelled) {
                    level.setBlock(down()!!.position, ev.newState!!, false, true)
                } else {
                    return type
                }
            } else if (down()!!.isAir) {
                val block = BlockCaveVines()
                block.growth = 0
                val ev = BlockGrowEvent(this, block)
                instance!!.pluginManager.callEvent(ev)
                if (!ev.isCancelled) {
                    level.setBlock(down()!!.position, ev.newState!!, false, true)
                }
            }
            return type
        }
        return 0
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (item.isFertilizer) {
            val block = if (up() is BlockCaveVines && down() !is BlockCaveVines) {
                BlockCaveVinesHeadWithBerries()
            } else BlockCaveVinesBodyWithBerries()
            val max = maxGrowth
            val growth = growth
            if (growth < max) {
                block.growth = max
                val ev = BlockGrowEvent(this, block)
                instance!!.pluginManager.callEvent(ev)
                if (ev.isCancelled) {
                    return false
                }
                level.setBlock(this.position, ev.newState!!, false, true)
                level.addParticle(BoneMealParticle(this.position))
                if (player != null && !player.isCreative) {
                    item.count--
                }
            }
            return true
        }
        if (item.isNull) {
            if (this.growth == 25) {
                val block = BlockCaveVines()
                block.growth = 0
                level.setBlock(this.position, block, false, true)
                level.dropItem(this.position, Item.get(ItemID.GLOW_BERRIES))
            }
            return true
        }
        return false
    }

    override fun getDrops(item: Item): Array<Item?>? {
        return Item.EMPTY_ARRAY
    }

    private val maxGrowth: Int
        get() = CommonBlockProperties.GROWING_PLANT_AGE.getMax()

    private var growth: Int
        get() = getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.GROWING_PLANT_AGE)
        private set(growth) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.GROWING_PLANT_AGE, growth)
        }

    companion object {
        val properties: BlockProperties = BlockProperties(CAVE_VINES, CommonBlockProperties.GROWING_PLANT_AGE)
            get() = Companion.field

        fun isValidSupport(block: Block): Boolean {
            return if (block is BlockLiquid) false
            else block.up()!!.isSolid || block.up() is BlockCaveVines
        }
    }
}
