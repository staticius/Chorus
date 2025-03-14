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

class BlockKelp @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFlowable(blockstate) {
    override val name: String
        get() = "Kelp"

    var age: Int
        get() = getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.KELP_AGE)
        set(age) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.KELP_AGE, age)
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
        val down = down()
        val layer1Block = block.getLevelBlockAtLayer(1)
        if ((down!!.id == BlockID.Companion.KELP || down.isSolid) && (down.id != BlockID.Companion.MAGMA) && (down.id != BlockID.Companion.ICE) && (down.id != BlockID.Companion.SOUL_SAND) &&
            (layer1Block is BlockFlowingWater && layer1Block.isSourceOrFlowingDown)
        ) {
            if (layer1Block.isFlowingDown) {
                level.setBlock(this.position, 1, get(BlockID.Companion.FLOWING_WATER), true, false)
            }

            val maxAge: Int = CommonBlockProperties.KELP_AGE.getMax()
            if (down.id == BlockID.Companion.KELP && down.getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.KELP_AGE) != maxAge - 1) {
                age = maxAge - 1
                level.setBlock(down.position, down, true, true)
            }

            //Placing it by hand gives it a random age value between 0 and 24.
            age = ThreadLocalRandom.current().nextInt(maxAge)
            level.setBlock(this.position, this, true, true)
            return true
        } else {
            return false
        }
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            val blockLayer1 = getLevelBlockAtLayer(1)
            if (blockLayer1 !is BlockFrostedIce &&
                (blockLayer1 !is BlockFlowingWater || !blockLayer1.isSourceOrFlowingDown)
            ) {
                level.useBreakOn(this.position)
                return type
            }

            val down = down()
            if ((!down!!.isSolid && down.id != BlockID.Companion.KELP) || down.id == BlockID.Companion.MAGMA || down.id == BlockID.Companion.ICE || down.id == BlockID.Companion.SOUL_SAND) {
                level.useBreakOn(this.position)
                return type
            }

            if (blockLayer1 is BlockFlowingWater && blockLayer1.isFlowingDown) {
                level.setBlock(this.position, 1, get(BlockID.Companion.FLOWING_WATER), true, false)
            }
            return type
        } else if (type == Level.BLOCK_UPDATE_RANDOM) {
            if (ThreadLocalRandom.current().nextInt(100) <= 14) {
                grow()
            }
            return type
        }
        return super.onUpdate(type)
    }

    fun grow(): Boolean {
        val age = age
        val maxValue: Int = CommonBlockProperties.KELP_AGE.getMax()
        if (age < maxValue) {
            val up = up()
            if (up is BlockFlowingWater && up.isSourceOrFlowingDown) {
                val grown: Block = BlockKelp(
                    blockState!!.setPropertyValue(
                        Companion.properties, CommonBlockProperties.KELP_AGE.createValue(
                            this.age + 1
                        )
                    )
                )
                val ev = BlockGrowEvent(this, grown)
                instance.pluginManager.callEvent(ev)
                if (!ev.isCancelled) {
                    this.age = maxValue
                    level.setBlock(this.position, 0, this, true, true)
                    level.setBlock(up.position, 1, get(BlockID.Companion.FLOWING_WATER), true, false)
                    level.setBlock(up.position, 0, ev.newState!!, true, true)
                    return true
                }
            }
        }
        return false
    }

    override fun onBreak(item: Item?): Boolean {
        val down = down()
        if (down!!.id == BlockID.Companion.KELP) {
            val blockKelp = BlockKelp(
                blockState!!.setPropertyValue(
                    Companion.properties, CommonBlockProperties.KELP_AGE.createValue(
                        ThreadLocalRandom.current().nextInt(
                            CommonBlockProperties.KELP_AGE.getMax()
                        )
                    )
                )
            )
            level.setBlock(down.position, blockKelp, true, true)
        }
        level.setBlock(this.position, get(BlockID.Companion.AIR), true, true)
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
        //Bone meal
        if (item.isFertilizer) {
            val x = position.x.toInt()
            val z = position.z.toInt()
            for (y in position.y.toInt() + 1..254) {
                val blockAbove = level.getBlock(x, y, z)
                val BlockID.bove = blockAbove!!.id
                if (BlockID.bove != BlockID.Companion.KELP) {
                    if (blockAbove is BlockFlowingWater) {
                        if (blockAbove.isSourceOrFlowingDown) {
                            val highestKelp = level.getBlock(x, y - 1, z) as BlockKelp?
                            if (highestKelp!!.grow()) {
                                level.addParticle(BoneMealParticle(this.position))

                                if (player != null && (player.gamemode and 0x01) == 0) {
                                    item.count--
                                }

                                return true
                            }
                        }
                    }
                    return false
                }
            }

            return true
        }

        return false
    }

    override val waterloggingLevel: Int
        get() = 2

    override fun canBeActivated(): Boolean {
        return true
    }

    override val isFertilizable: Boolean
        get() = true

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.Companion.KELP, CommonBlockProperties.KELP_AGE)

    }
}
