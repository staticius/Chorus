package cn.nukkit.entity.mob.monster

import cn.nukkit.Player
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.EntityCheckEvaluator
import cn.nukkit.entity.ai.evaluator.IBehaviorEvaluator
import cn.nukkit.entity.ai.evaluator.MemoryCheckNotEmptyEvaluator
import cn.nukkit.entity.ai.executor.EntityExplosionExecutor
import cn.nukkit.entity.ai.executor.FlatRandomRoamExecutor
import cn.nukkit.entity.ai.executor.FleeFromTargetExecutor
import cn.nukkit.entity.ai.executor.MoveToTargetExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestEntitySensor
import cn.nukkit.entity.ai.sensor.NearestPlayerSensor
import cn.nukkit.entity.data.EntityDataTypes
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.mob.animal.EntityCat
import cn.nukkit.entity.mob.animal.EntityOcelot
import cn.nukkit.entity.weather.EntityLightningStrike
import cn.nukkit.event.entity.CreeperPowerEvent
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.item.*
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.math.*
import cn.nukkit.nbt.tag.CompoundTag
import java.util.Set
import java.util.concurrent.*

/**
 * @author Box.
 */
class EntityCreeper(chunk: IChunk?, nbt: CompoundTag) : EntityMonster(chunk, nbt), EntityWalkable, EntityInteractable {
    override fun getIdentifier(): String {
        return EntityID.Companion.CREEPER
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            Set.of<IBehavior>(
                Behavior(
                    FleeFromTargetExecutor(CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY, 0.3f, true, 4f),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY),
                    5,
                    1
                ),
                Behavior(
                    EntityExplosionExecutor(30, 3, CoreMemoryTypes.Companion.SHOULD_EXPLODE),
                    all(
                        IBehaviorEvaluator { entity: EntityMob ->
                            entity.memoryStorage!!
                                .compareDataTo<Boolean>(CoreMemoryTypes.Companion.SHOULD_EXPLODE, true)
                        },
                        any(
                            IBehaviorEvaluator { entity: EntityMob? ->
                                memoryStorage!!.get<Player?>(CoreMemoryTypes.Companion.NEAREST_PLAYER) != null && level!!.raycastBlocks(
                                    this.position,
                                    memoryStorage!!.get<Player>(CoreMemoryTypes.Companion.NEAREST_PLAYER).position
                                ).isEmpty()
                            },
                            IBehaviorEvaluator { entity: EntityMob? ->
                                memoryStorage!!.get<Entity?>(CoreMemoryTypes.Companion.ATTACK_TARGET) != null && level!!.raycastBlocks(
                                    this.position,
                                    memoryStorage!!.get<Entity>(CoreMemoryTypes.Companion.ATTACK_TARGET).position
                                ).isEmpty()
                            }
                        )
                    ), 4, 1
                ),
                Behavior(
                    MoveToTargetExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.3f, true, 16f, 3f, true), all(
                        MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                        IBehaviorEvaluator { entity: EntityMob ->
                            !entity.memoryStorage!!.notEmpty(CoreMemoryTypes.Companion.ATTACK_TARGET) || (entity.memoryStorage!!
                                .get<Entity>(CoreMemoryTypes.Companion.ATTACK_TARGET) !is Player) || player.isSurvival()
                        }
                    ), 3, 1),
                Behavior(
                    MoveToTargetExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.3f, true, 16f, 3f), all(
                        MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                        IBehaviorEvaluator { entity: EntityMob ->
                            if (entity.memoryStorage!!.isEmpty(CoreMemoryTypes.Companion.NEAREST_PLAYER)) return@all true
                            val player = entity.memoryStorage!!.get<Player>(CoreMemoryTypes.Companion.NEAREST_PLAYER)
                            player!!.isSurvival
                        }
                    ), 2, 1),
                Behavior(FlatRandomRoamExecutor(0.3f, 12, 100, false, -1, true, 10), none(), 1, 1)
            ),
            Set.of<ISensor>(
                NearestPlayerSensor(16.0, 0.0, 20),
                NearestEntitySensor(EntityCat::class.java, CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY, 42.0, 0.0),
                NearestEntitySensor(
                    EntityOcelot::class.java,
                    CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY,
                    42.0,
                    0.0
                ),
                ISensor { entity: EntityMob ->
                    val memoryStorage = entity.memoryStorage
                    var attacker = memoryStorage!!.get<Entity>(CoreMemoryTypes.Companion.ATTACK_TARGET)
                    if (attacker == null) attacker = memoryStorage.get<Player>(CoreMemoryTypes.Companion.NEAREST_PLAYER)
                    if (attacker != null && (attacker !is Player || attacker.isSurvival) && attacker.position.distanceSquared(
                            entity.position
                        ) <= 3 * 3 && (memoryStorage.isEmpty(CoreMemoryTypes.Companion.SHOULD_EXPLODE) || memoryStorage.compareDataTo<Boolean>(
                            CoreMemoryTypes.Companion.SHOULD_EXPLODE,
                            false
                        ))
                    ) {
                        memoryStorage.put<Boolean>(CoreMemoryTypes.Companion.SHOULD_EXPLODE, true)
                        return@of
                    }
                    if ((attacker == null || (attacker is Player && !attacker.isSurvival) || attacker.position.distanceSquared(
                            entity.position
                        ) >= 7 * 7) && memoryStorage.compareDataTo<Boolean>(
                            CoreMemoryTypes.Companion.SHOULD_EXPLODE,
                            true
                        ) && memoryStorage.get<Boolean>(CoreMemoryTypes.Companion.EXPLODE_CANCELLABLE)
                    ) {
                        memoryStorage.put<Boolean>(CoreMemoryTypes.Companion.SHOULD_EXPLODE, false)
                    }
                }),
            Set.of<IController>(WalkController(), LookController(true, true), FluctuateController()),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }


    override fun getWidth(): Float {
        return 0.6f
    }

    override fun getHeight(): Float {
        return 1.8f
    }

    override fun getFloatingHeight(): Float {
        return 0.6f
    }

    fun isPowered(): Boolean {
        return getDataProperty<Byte>(EntityDataTypes.Companion.HORSE_TYPE!!) > 0
    }

    fun setPowered(bolt: EntityLightningStrike?) {
        val ev = CreeperPowerEvent(this, bolt, CreeperPowerEvent.PowerCause.LIGHTNING)
        getServer()!!.pluginManager.callEvent(ev)

        if (!ev.isCancelled) {
            this.setDataProperty(EntityDataTypes.Companion.HORSE_TYPE, 1)
            namedTag!!.putBoolean("powered", true)
        }
    }

    fun setPowered(powered: Boolean) {
        val ev = CreeperPowerEvent(
            this,
            if (powered) CreeperPowerEvent.PowerCause.SET_ON else CreeperPowerEvent.PowerCause.SET_OFF
        )
        getServer()!!.pluginManager.callEvent(ev)

        if (!ev.isCancelled) {
            this.setDataProperty(EntityDataTypes.Companion.HORSE_TYPE, if (powered) 1 else 0)
            namedTag!!.putBoolean("powered", powered)
        }
    }

    override fun onStruckByLightning(entity: Entity) {
        this.setPowered(true)
    }

    override fun initEntity() {
        this.maxHealth = 20
        super.initEntity()

        if (namedTag!!.getBoolean("powered") || namedTag!!.getBoolean("IsPowered")) {
            entityDataMap[EntityDataTypes.Companion.HORSE_TYPE] = 1
        }
    }

    override fun getOriginalName(): String {
        return "Creeper"
    }

    override fun getDrops(): Array<Item?> {
        if (lastDamageCause is EntityDamageByEntityEvent) {
            return arrayOf(Item.get(Item.GUNPOWDER, 0, ThreadLocalRandom.current().nextInt(2) + 1))
        }
        return Item.EMPTY_ARRAY
    }

    override fun isPreventingSleep(player: Player?): Boolean {
        return true
    }

    override fun onInteract(player: Player, item: Item, clickedPos: Vector3): Boolean {
        val memoryStorage = this.memoryStorage
        if (item.id === Item.FLINT_AND_STEEL && (memoryStorage!!.isEmpty(CoreMemoryTypes.Companion.SHOULD_EXPLODE) || memoryStorage.compareDataTo<Boolean>(
                CoreMemoryTypes.Companion.SHOULD_EXPLODE,
                false
            ))
        ) {
            memoryStorage.put<Boolean>(CoreMemoryTypes.Companion.SHOULD_EXPLODE, true)
            memoryStorage.put<Boolean>(CoreMemoryTypes.Companion.EXPLODE_CANCELLABLE, false)
            level!!.addSound(this.position, Sound.FIRE_IGNITE)
            return true
        }
        return super.onInteract(player, item, clickedPos)
    }

    override fun getInteractButtonText(player: Player): String {
        return "action.interact.creeper"
    }

    override fun canDoInteraction(): Boolean {
        return true
    }
}
