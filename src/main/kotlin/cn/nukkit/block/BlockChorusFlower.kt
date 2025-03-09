package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.Server.Companion.instance
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.type.IntPropertyType
import cn.nukkit.entity.Entity
import cn.nukkit.entity.projectile.EntitySmallFireball
import cn.nukkit.entity.projectile.abstract_arrow.EntityArrow
import cn.nukkit.event.block.BlockGrowEvent
import cn.nukkit.item.*
import cn.nukkit.level.Level
import cn.nukkit.level.Locator
import cn.nukkit.level.Sound
import cn.nukkit.math.*
import java.util.concurrent.ThreadLocalRandom

class BlockChorusFlower @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockTransparent(blockstate) {
    override val name: String
        get() = "Chorus Flower"

    override val hardness: Double
        get() = 0.4

    override val resistance: Double
        get() = 0.4

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    private val isPositionValid: Boolean
        get() {
            // Chorus flowers must be above end stone or chorus plant, or be above air and horizontally adjacent to exactly one chorus plant.
            // If these conditions are not met, the block breaks without dropping anything.
            val down = down()
            if (down!!.id == CHORUS_PLANT || down.id == END_STONE) {
                return true
            }
            if (!down.isAir) {
                return false
            }
            var foundPlant = false
            for (face in BlockFace.Plane.HORIZONTAL) {
                val side = getSide(face)
                if (side!!.id == CHORUS_PLANT) {
                    if (foundPlant) {
                        return false
                    }
                    foundPlant = true
                }
            }

            return foundPlant
        }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (!isPositionValid) {
                level.scheduleUpdate(this, 1)
                return type
            }
        } else if (type == Level.BLOCK_UPDATE_SCHEDULED) {
            level.useBreakOn(this.position, null, null, true)
            return type
        } else if (type == Level.BLOCK_UPDATE_RANDOM) {
            // Check limit
            if (up()!!.isAir && up()!!.y <= level.maxHeight) {
                if (!isFullyAged) {
                    var growUp = false // Grow upward?
                    var ground = false // Is on the ground directly?
                    if (down()!!.isAir || down()!!.id == END_STONE) {
                        growUp = true
                    } else if (down()!!.id == CHORUS_PLANT) {
                        var height = 1
                        for (y in 2..5) {
                            if (down(y)!!.id == CHORUS_PLANT) {
                                height++
                            } else {
                                if (down(y)!!.id == END_STONE) {
                                    ground = true
                                }
                                break
                            }
                        }

                        if (height < 2 || height <= ThreadLocalRandom.current().nextInt(if (ground) 5 else 4)) {
                            growUp = true
                        }
                    }


                    // Grow Upward
                    if (growUp && up(2)!!.isAir && isHorizontalAir(up()!!)) {
                        val block = clone() as BlockChorusFlower
                        block.position.y = position.y + 1
                        val ev = BlockGrowEvent(this, block)
                        instance!!.pluginManager.callEvent(ev)

                        if (!ev.isCancelled) {
                            level.setBlock(this.position, get(CHORUS_PLANT))
                            level.setBlock(block.position, ev.newState!!)
                            level.addSound(position.add(0.5, 0.5, 0.5)!!, Sound.BLOCK_CHORUSFLOWER_GROW)
                        } else {
                            return Level.BLOCK_UPDATE_RANDOM
                        }
                        // Grow Horizontally
                    } else if (!isFullyAged) {
                        for (i in 0..<ThreadLocalRandom.current().nextInt(if (ground) 5 else 4)) {
                            val face = BlockFace.Plane.HORIZONTAL.random()
                            val check = this.getSide(face)
                            if (check!!.isAir && check.down()!!.isAir && isHorizontalAirExcept(
                                    check,
                                    face.getOpposite()
                                )
                            ) {
                                val block = clone() as BlockChorusFlower
                                block.position.x = check.position.x
                                block.position.y = check.position.y
                                block.position.z = check.position.z
                                block.age = age + 1
                                val ev = BlockGrowEvent(this, block)
                                instance!!.pluginManager.callEvent(ev)

                                if (!ev.isCancelled) {
                                    level.setBlock(this.position, get(CHORUS_PLANT))
                                    level.setBlock(block.position, ev.newState!!)
                                    level.addSound(position.add(0.5, 0.5, 0.5)!!, Sound.BLOCK_CHORUSFLOWER_GROW)
                                } else {
                                    return Level.BLOCK_UPDATE_RANDOM
                                }
                            }
                        }
                        // Death
                    } else {
                        val block = clone() as BlockChorusFlower
                        block.age = maxAge
                        val ev = BlockGrowEvent(this, block)
                        instance!!.pluginManager.callEvent(ev)

                        if (!ev.isCancelled) {
                            level.setBlock(block.position, ev.newState!!)
                            level.addSound(position.add(0.5, 0.5, 0.5)!!, Sound.BLOCK_CHORUSFLOWER_DEATH)
                        } else {
                            return Level.BLOCK_UPDATE_RANDOM
                        }
                    }
                }
            } else {
                return Level.BLOCK_UPDATE_RANDOM
            }
        }

        return 0
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
        if (!isPositionValid) {
            return false
        }
        return super.place(item, block, target, face, fx, fy, fz, player)
    }

    override fun getDrops(item: Item): Array<Item?>? {
        return arrayOf(this.toItem())
    }

    override fun breaksWhenMoved(): Boolean {
        return true
    }

    override fun sticksToPiston(): Boolean {
        return false
    }

    override fun onProjectileHit(projectile: Entity, locator: Locator, motion: Vector3): Boolean {
        if (projectile is EntityArrow || projectile is EntitySnowball || projectile is EntitySmallFireball) {
            level.useBreakOn(this.position)
            return true
        }
        return super.onProjectileHit(projectile, locator, motion)
    }

    val maxAge: Int
        get() = CommonBlockProperties.AGE_6.getMax()

    var age: Int
        get() = getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.AGE_6)
        set(age) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.AGE_6, age)
        }

    val isFullyAged: Boolean
        get() = age >= maxAge

    private fun isHorizontalAir(block: Block): Boolean {
        for (face in BlockFace.Plane.HORIZONTAL) {
            if (!block.getSide(face)!!.isAir) {
                return false
            }
        }
        return true
    }

    private fun isHorizontalAirExcept(block: Block, except: BlockFace?): Boolean {
        for (face in BlockFace.Plane.HORIZONTAL) {
            if (face != except) {
                if (!block.getSide(face)!!.isAir) {
                    return false
                }
            }
        }
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(CHORUS_FLOWER, CommonBlockProperties.AGE_6)
            get() = Companion.field
    }
}
