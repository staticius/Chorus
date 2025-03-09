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
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.min

/**
 * @author MagicDroidX (Nukkit Project)
 */
abstract class BlockCrops(blockState: BlockState?) : BlockFlowable(blockState) {
    val maxGrowth: Int
        get() = CommonBlockProperties.GROWTH.getMax()

    var growth: Int
        get() = getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.GROWTH)
        set(growth) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.GROWTH, growth)
        }

    val isFullyGrown: Boolean
        get() = growth >= maxGrowth

    override fun canBeActivated(): Boolean {
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
        if (block.down()!!.id == FARMLAND) {
            level.setBlock(block.position, this, true, true)
            return true
        }
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
        //Bone meal
        if (item.isFertilizer) {
            val max = maxGrowth
            var growth = growth
            if (growth < max) {
                val block = clone() as BlockCrops
                growth += ThreadLocalRandom.current().nextInt(3) + 2
                block.growth = min(growth.toDouble(), max.toDouble()).toInt()
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

        return false
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (down()!!.id != FARMLAND) {
                level.useBreakOn(this.position)
                return Level.BLOCK_UPDATE_NORMAL
            }
        } else if (type == Level.BLOCK_UPDATE_RANDOM) {
            if (ThreadLocalRandom.current()
                    .nextInt(2) == 1 && level.getFullLight(this.position) >= this.minimumLightLevel
            ) {
                val growth = growth
                if (growth < maxGrowth) {
                    val block = clone() as BlockCrops
                    block.growth = growth + 1
                    val ev = BlockGrowEvent(this, block)
                    instance!!.pluginManager.callEvent(ev)

                    if (!ev.isCancelled) {
                        level.setBlock(this.position, ev.newState!!, false, true)
                    } else {
                        return Level.BLOCK_UPDATE_RANDOM
                    }
                }
            } else {
                return Level.BLOCK_UPDATE_RANDOM
            }
        }

        return 0
    }

    override val isFertilizable: Boolean
        get() = true

    companion object {
        val minimumLightLevel: Int = 9
            get() = Companion.field
    }
}
