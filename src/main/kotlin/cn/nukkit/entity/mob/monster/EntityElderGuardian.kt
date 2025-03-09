package cn.nukkit.entity.mob.monster

import cn.nukkit.Player
import cn.nukkit.block.*
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.EntityCheckEvaluator
import cn.nukkit.entity.ai.evaluator.IBehaviorEvaluator
import cn.nukkit.entity.ai.evaluator.PassByTimeEvaluator
import cn.nukkit.entity.ai.evaluator.RandomSoundEvaluator
import cn.nukkit.entity.ai.executor.FleeFromTargetExecutor
import cn.nukkit.entity.ai.executor.GuardianAttackExecutor
import cn.nukkit.entity.ai.executor.PlaySoundExecutor
import cn.nukkit.entity.ai.executor.SpaceRandomRoamExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.memory.MemoryType
import cn.nukkit.entity.ai.route.finder.impl.SimpleSpaceAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.SwimmingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestPlayerSensor
import cn.nukkit.entity.ai.sensor.NearestTargetEntitySensor
import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.entity.effect.*
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause
import cn.nukkit.item.*
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.protocol.LevelEventPacket
import cn.nukkit.utils.*
import java.util.List
import java.util.Set
import java.util.concurrent.*
import java.util.function.Function

/**
 * @author PikyCZ
 */
class EntityElderGuardian(chunk: IChunk?, nbt: CompoundTag) : EntityMonster(chunk, nbt), EntitySwimmable {
    override fun getIdentifier(): String {
        return EntityID.Companion.ELDER_GUARDIAN
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            Set.of<IBehavior>(
                Behavior(
                    PlaySoundExecutor(Sound.MOB_ELDERGUARDIAN_IDLE, 0.8f, 1.2f, 1f, 1f),
                    all(IBehaviorEvaluator { entity: EntityMob? -> isInsideOfWater }, RandomSoundEvaluator()),
                    6,
                    1,
                    1,
                    true
                ),
                Behavior(
                    PlaySoundExecutor(Sound.MOB_GUARDIAN_LAND_IDLE, 0.8f, 1.2f, 1f, 1f),
                    all(IBehaviorEvaluator { entity: EntityMob? -> !isInsideOfWater }, RandomSoundEvaluator()),
                    5,
                    1,
                    1,
                    true
                ),
                Behavior(
                    FleeFromTargetExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.5f, true, 9f), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_BE_ATTACKED_TIME, 0, 100)
                    ), 4, 1
                ),
                Behavior(
                    GuardianAttackExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.3f, 15, true, 60, 40), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                        IBehaviorEvaluator { entity: EntityMob ->
                            entity.memoryStorage!!.get<Player?>(CoreMemoryTypes.Companion.NEAREST_PLAYER) != null && !entity.memoryStorage!!
                                .get<Player>(CoreMemoryTypes.Companion.NEAREST_PLAYER).isBlocking
                        },
                        IBehaviorEvaluator { entity: EntityMob ->
                            entity.memoryStorage!!.get<Player?>(CoreMemoryTypes.Companion.NEAREST_PLAYER) != null && level!!.raycastBlocks(
                                entity.position, entity.memoryStorage!!
                                    .get<Player>(CoreMemoryTypes.Companion.NEAREST_PLAYER).position
                            ).stream().allMatch { obj: Block -> obj.isTransparent }
                        }
                    ), 3, 1),
                Behavior(
                    GuardianAttackExecutor(
                        CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET,
                        0.3f,
                        15,
                        true,
                        60,
                        40
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET), 2, 1
                ),
                Behavior(SpaceRandomRoamExecutor(0.36f, 12, 1, 80, false, -1, false, 10), none(), 1, 1)
            ),
            Set.of<ISensor>(
                NearestPlayerSensor(40.0, 0.0, 20),
                NearestTargetEntitySensor<Entity>(
                    0.0, 16.0, 20,
                    List.of<MemoryType<Entity?>?>(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                    Function<Entity, Boolean> { entity: Entity? ->
                        this.attackTarget(
                            entity!!
                        )
                    })
            ),
            Set.of<IController>(SpaceMoveController(), LookController(true, true), DiveController()),
            SimpleSpaceAStarRouteFinder(SwimmingPosEvaluator(), this),
            this
        )
    }


    override fun initEntity() {
        this.maxHealth = 80
        super.initEntity()
        this.diffHandDamage = floatArrayOf(5f, 8f, 12f)
        this.setDataFlag(EntityFlag.ELDER, true)
    }

    override fun getWidth(): Float {
        return 1.99f
    }

    override fun getHeight(): Float {
        return 1.99f
    }

    override fun getOriginalName(): String {
        return "Elder Guardian"
    }

    override fun isPreventingSleep(player: Player?): Boolean {
        return true
    }

    override fun getDrops(): Array<Item?> {
        val secondLoot = ThreadLocalRandom.current().nextInt(6)
        return arrayOf(
            Item.get(Item.PRISMARINE_SHARD, 0, Utils.rand(0, 2)),
            Item.get(Block.WET_SPONGE, 0, 1),
            if (ThreadLocalRandom.current().nextInt(100) < 20) Item.get(
                Item.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE,
                0,
                1
            ) else Item.AIR,
            if (ThreadLocalRandom.current().nextInt(1000) < 25) Item.get(Item.COD, 0, 1) else Item.AIR,
            if (secondLoot <= 2) Item.get(Item.COD, 0, Utils.rand(0, 1)) else Item.AIR,
            if (secondLoot > 2 && secondLoot <= 4) Item.get(Item.PRISMARINE_CRYSTALS, 0, Utils.rand(0, 1)) else Item.AIR
        )
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        if (source.cause == DamageCause.SUFFOCATION) {
            return false
        }
        if (super.attack(source)) {
            if (source is EntityDamageByEntityEvent) {
                source.damager.attack(
                    EntityDamageByEntityEvent(
                        this,
                        source.getEntity(),
                        DamageCause.THORNS,
                        (if (getServer()!!.difficulty == 3) 2 else 3).toFloat()
                    )
                )
            }
            return true
        }
        return false
    }

    override fun onUpdate(currentTick: Int): Boolean {
        if (!this.closed && this.isAlive) {
            for (p in this.viewers.values) {
                if (p.locallyInitialized && p.getGamemode() % 2 == 0 && p.position.distance(this.position) < 50 && !p.hasEffect(
                        EffectType.Companion.MINING_FATIGUE
                    )
                ) {
                    p.addEffect(
                        Effect.Companion.get(EffectType.Companion.MINING_FATIGUE).setAmplifier(2).setDuration(6000)
                    )
                    val pk = LevelEventPacket()
                    pk.evid = LevelEventPacket.EVENT_PARTICLE_SOUND_GUARDIAN_GHOST
                    pk.x = position.x.toFloat()
                    pk.y = position.y.toFloat()
                    pk.z = position.z.toFloat()
                    p.dataPacket(pk)
                }
            }
        }
        return super.onUpdate(currentTick)
    }

    override fun getExperienceDrops(): Int {
        return 10
    }

    override fun attackTarget(entity: Entity): Boolean {
        return when (entity.getIdentifier()) {
            EntityID.Companion.SQUID, EntityID.Companion.GLOW_SQUID, EntityID.Companion.AXOLOTL -> true
            else -> false
        }
    }
}
