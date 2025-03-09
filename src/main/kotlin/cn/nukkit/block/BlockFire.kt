package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.Server.Companion.instance
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.type.IntPropertyType
import cn.nukkit.entity.Entity
import cn.nukkit.entity.effect.EffectType
import cn.nukkit.entity.projectile.abstract_arrow.EntityArrow
import cn.nukkit.event.block.BlockBurnEvent
import cn.nukkit.event.block.BlockFadeEvent
import cn.nukkit.event.block.BlockIgniteEvent
import cn.nukkit.event.entity.EntityCombustByBlockEvent
import cn.nukkit.event.entity.EntityDamageByBlockEvent
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause
import cn.nukkit.item.*
import cn.nukkit.level.GameRule
import cn.nukkit.level.Level
import cn.nukkit.math.*
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.max
import kotlin.math.min

/**
 * @author MagicDroidX (Nukkit Project)
 */
open class BlockFire @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFlowable(blockstate) {
    var age: Int
        get() = getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.AGE_16)
        set(age) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.AGE_16, age)
        }

    override fun hasEntityCollision(): Boolean {
        return true
    }

    override val name: String
        get() = "Fire Block"

    override val lightLevel: Int
        get() = 15

    override fun isBreakable(vector: Vector3, layer: Int, face: BlockFace?, item: Item?, player: Player?): Boolean {
        return false
    }

    override fun canBeReplaced(): Boolean {
        return true
    }

    override fun onEntityCollide(entity: Entity) {
        if (!entity.hasEffect(EffectType.FIRE_RESISTANCE)) {
            entity.attack(EntityDamageByBlockEvent(this, entity, DamageCause.FIRE, 1f))
        }

        val ev = EntityCombustByBlockEvent(this, entity, 8)
        if (entity is EntityArrow) {
            ev.setCancelled()
        }
        instance!!.pluginManager.callEvent(ev)
        if (!ev.isCancelled && entity.isAlive() && entity.noDamageTicks == 0) {
            entity.setOnFire(ev.duration)
        }
    }

    override fun getDrops(item: Item): Array<Item?>? {
        return Item.EMPTY_ARRAY
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL || type == Level.BLOCK_UPDATE_RANDOM) {
            val down = down()

            if (type == Level.BLOCK_UPDATE_NORMAL) {
                val downId = down!!.id
                if (downId == Block.SOUL_SAND || downId == Block.SOUL_SOIL) {
                    level.setBlock(
                        this.position, get(SOUL_FIRE).setPropertyValue<Int, IntPropertyType>(
                            CommonBlockProperties.AGE_16,
                            age
                        )
                    )
                    return type
                }
            }

            if (!this.isBlockTopFacingSurfaceSolid(down) && !this.canNeighborBurn()) {
                val event = BlockFadeEvent(this, get(AIR))
                level.server.pluginManager.callEvent(event)
                if (!event.isCancelled) {
                    level.setBlock(this.position, event.newState, true)
                }
            } else if (level.gameRules!!.getBoolean(GameRule.DO_FIRE_TICK) && !level.isUpdateScheduled(
                    this.position,
                    this
                )
            ) {
                level.scheduleUpdate(this, tickRate())
            }

            //在第一次放置时就检查下雨
            checkRain()

            return Level.BLOCK_UPDATE_NORMAL
        } else if (type == Level.BLOCK_UPDATE_SCHEDULED && level.gameRules!!.getBoolean(GameRule.DO_FIRE_TICK)) {
            val down = down()
            val downId = down!!.id

            val random = ThreadLocalRandom.current()

            //TODO: END
            if (!this.isBlockTopFacingSurfaceSolid(down) && !this.canNeighborBurn()) {
                val event = BlockFadeEvent(this, get(AIR))
                level.server.pluginManager.callEvent(event)
                if (!event.isCancelled) {
                    level.setBlock(this.position, event.newState, true)
                }
            }

            val forever = downId == NETHERRACK ||
                    downId == MAGMA || downId == BEDROCK && (down as BlockBedrock).burnIndefinitely

            if (!checkRain()) {
                val meta = age

                if (meta < 15) {
                    val newMeta = meta + random.nextInt(3)
                    this.age = min(newMeta.toDouble(), 15.0).toInt()
                    level.setBlock(this.position, this, true)
                }

                level.scheduleUpdate(this, this.tickRate() + random.nextInt(10))

                if (!forever && !this.canNeighborBurn()) {
                    if (!this.isBlockTopFacingSurfaceSolid(down) || meta > 3) {
                        val event = BlockFadeEvent(this, get(AIR))
                        level.server.pluginManager.callEvent(event)
                        if (!event.isCancelled) {
                            level.setBlock(this.position, event.newState, true)
                        }
                    }
                } else if (!forever && (down.burnAbility <= 0) && meta == 15 && random.nextInt(4) == 0) {
                    val event = BlockFadeEvent(this, get(AIR))
                    level.server.pluginManager.callEvent(event)
                    if (!event.isCancelled) {
                        level.setBlock(this.position, event.newState, true)
                    }
                } else {
                    val o = 0

                    //TODO: decrease the o if the rainfall values are high
                    this.tryToCatchBlockOnFire(east()!!, 300 + o, meta)
                    this.tryToCatchBlockOnFire(west()!!, 300 + o, meta)
                    this.tryToCatchBlockOnFire(down, 250 + o, meta)
                    this.tryToCatchBlockOnFire(up()!!, 250 + o, meta)
                    this.tryToCatchBlockOnFire(south()!!, 300 + o, meta)
                    this.tryToCatchBlockOnFire(north()!!, 300 + o, meta)

                    for (x in (position.x - 1).toInt()..(position.x + 1).toInt()) {
                        for (z in (position.z - 1).toInt()..(position.z + 1).toInt()) {
                            for (y in (position.y - 1).toInt()..(position.y + 4).toInt()) {
                                if (x != position.x.toInt() || y != position.y.toInt() || z != position.z.toInt()) {
                                    var k = 100

                                    if (y > position.y + 1) {
                                        k += ((y - (position.y + 1)) * 100).toInt()
                                    }

                                    val block =
                                        level.getBlock(Vector3(x.toDouble(), y.toDouble(), z.toDouble()))
                                    val chance = this.getChanceOfNeighborsEncouragingFire(
                                        block!!
                                    )

                                    if (chance > 0) {
                                        val t = (chance + 40 + level.server.getDifficulty() * 7) / (meta + 30)

                                        //TODO: decrease the t if the rainfall values are high
                                        if (t > 0 && random.nextInt(k) <= t) {
                                            var damage = meta + random.nextInt(5) / 4

                                            if (damage > 15) {
                                                damage = 15
                                            }

                                            val e = BlockIgniteEvent(
                                                block,
                                                this,
                                                null,
                                                BlockIgniteEvent.BlockIgniteCause.SPREAD
                                            )
                                            level.server.pluginManager.callEvent(e)

                                            if (!e.isCancelled) {
                                                level.setBlock(
                                                    block.position, get(
                                                        blockState!!.setPropertyValue(
                                                            Companion.properties,
                                                            CommonBlockProperties.AGE_16.createValue(
                                                                age
                                                            )
                                                        )
                                                    ), true
                                                )
                                                level.scheduleUpdate(block, this.tickRate())
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return 0
    }

    private fun tryToCatchBlockOnFire(block: Block, bound: Int, damage: Int) {
        val burnAbility = block.burnAbility

        val random: Random = ThreadLocalRandom.current()

        if (random.nextInt(bound) < burnAbility) {
            if (random.nextInt(damage + 10) < 5) {
                var meta = damage + random.nextInt(5) / 4

                if (meta > 15) {
                    meta = 15
                }

                val e = BlockIgniteEvent(block, this, null, BlockIgniteEvent.BlockIgniteCause.SPREAD)
                level.server.pluginManager.callEvent(e)

                if (!e.isCancelled) {
                    level.setBlock(
                        block.position, get(
                            blockState!!.setPropertyValue(
                                Companion.properties, CommonBlockProperties.AGE_16.createValue(
                                    age
                                )
                            )
                        ), true
                    )
                    level.scheduleUpdate(block, this.tickRate())
                }
            } else {
                val ev = BlockBurnEvent(block)
                level.server.pluginManager.callEvent(ev)

                if (!ev.isCancelled) {
                    level.setBlock(block.position, get(AIR), true)
                }
            }

            if (block is BlockTnt) {
                block.prime()
            }
        }
    }

    private fun getChanceOfNeighborsEncouragingFire(block: Block): Int {
        if (!block.isAir) {
            return 0
        } else {
            var chance = 0
            chance = max(chance.toDouble(), block.east()!!.burnChance.toDouble()).toInt()
            chance = max(chance.toDouble(), block.west()!!.burnChance.toDouble()).toInt()
            chance = max(chance.toDouble(), block.down()!!.burnChance.toDouble()).toInt()
            chance = max(chance.toDouble(), block.up()!!.burnChance.toDouble()).toInt()
            chance = max(chance.toDouble(), block.south()!!.burnChance.toDouble()).toInt()
            chance = max(chance.toDouble(), block.north()!!.burnChance.toDouble()).toInt()
            return chance
        }
    }

    fun canNeighborBurn(): Boolean {
        for (face in BlockFace.entries) {
            if (getSide(face)!!.burnChance > 0) {
                return true
            }
        }

        return false
    }

    fun isBlockTopFacingSurfaceSolid(block: Block?): Boolean {
        if (block != null) {
            return if (block is BlockStairs) {
                false
            } else if (block is BlockSlab && !block.isOnTop) {
                false
            } else if (block is BlockSnowLayer) {
                false
            } else if (block is BlockFenceGate) {
                false
            } else if (block is BlockTrapdoor) {
                false
            } else if (block is BlockMossCarpet) {
                false
            } else if (block is BlockAzalea) {
                false
            } else block.isSolid
        }
        return false
    }

    override fun tickRate(): Int {
        return 30
    }

    override fun recalculateCollisionBoundingBox(): AxisAlignedBB? {
        return this
    }

    override fun toItem(): Item? {
        return ItemBlock(get(AIR))
    }

    /**
     * 检查火焰是否应被雨水浇灭
     *
     * @return 是否应被雨水浇灭
     */
    protected fun checkRain(): Boolean {
        val down = down()
        val downId = down!!.id
        val forever = downId == NETHERRACK || downId == MAGMA
                || downId == BEDROCK && (down as BlockBedrock).burnIndefinitely

        if (!forever && level.isRaining &&
            (level.canBlockSeeSky(this.position) ||
                    level.canBlockSeeSky(east()!!.position) ||
                    level.canBlockSeeSky(west()!!.position) ||
                    level.canBlockSeeSky(south()!!.position) ||
                    level.canBlockSeeSky(north()!!.position))
        ) {
            val event = BlockFadeEvent(this, get(AIR))
            level.server.pluginManager.callEvent(event)
            if (!event.isCancelled) {
                level.setBlock(this.position, event.newState, true)
            }
            return true
        }
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(FIRE, CommonBlockProperties.AGE_16)
            get() = Companion.field
    }
}
