package org.chorus.entity.mob.animal

import org.chorus.Player
import org.chorus.block.*
import org.chorus.entity.*
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.*
import org.chorus.entity.ai.evaluator.*
import org.chorus.entity.ai.executor.*
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.memory.MemoryType
import org.chorus.entity.ai.route.finder.IRouteFinder
import org.chorus.entity.ai.route.finder.impl.ConditionalAStarRouteFinder
import org.chorus.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import org.chorus.entity.ai.route.finder.impl.SimpleSpaceAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.SwimmingPosEvaluator
import org.chorus.entity.ai.route.posevaluator.WalkingPosEvaluator
import org.chorus.entity.ai.sensor.*
import org.chorus.entity.effect.*
import org.chorus.entity.mob.EntityMob
import org.chorus.event.entity.EntityDamageByEntityEvent
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.event.entity.EntityDamageEvent.DamageCause
import org.chorus.item.*
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.math.*
import org.chorus.nbt.tag.CompoundTag
import org.chorus.utils.*
import it.unimi.dsi.fastutil.Pair
import java.util.List
import java.util.Set
import java.util.function.Function
import java.util.function.Predicate

class EntityAxolotl(chunk: IChunk?, nbt: CompoundTag) : EntityAnimal(chunk, nbt), EntitySwimmable, EntityVariant,
    EntityCanAttack {
    override fun getIdentifier(): String {
        return EntityID.Companion.AXOLOTL
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            Set.of<IBehavior>( //用于刷新InLove状态的核心行为
                Behavior(
                    InLoveExecutor(400),
                    all(
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_BE_FEED_TIME, 0, 400),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_IN_LOVE_TIME, 6000, Int.MAX_VALUE)
                    ),
                    1, 1
                ),
                Behavior(
                    { entity: EntityMob? ->
                        moveTarget = memoryStorage.get<Block>(CoreMemoryTypes.Companion.NEAREST_BLOCK).position
                        true
                    }, all(
                        MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.NEAREST_BLOCK),
                        IBehaviorEvaluator { entity: EntityMob? -> !isInsideOfWater },
                        not(DistanceEvaluator(CoreMemoryTypes.Companion.NEAREST_BLOCK, 9.0))
                    ), 1, 1
                )
            ),
            Set.of<IBehavior>(
                Behavior(
                    PlaySoundExecutor(Sound.MOB_AXOLOTL_SPLASH), all(
                        IBehaviorEvaluator { entity: EntityMob? -> airTicks == 399 }
                    ), 7, 1),
                Behavior(
                    PlaySoundExecutor(Sound.MOB_AXOLOTL_IDLE_WATER), all(
                        RandomSoundEvaluator(),
                        IBehaviorEvaluator { entity: EntityMob? -> isInsideOfWater }), 7, 1
                ),
                Behavior(
                    PlaySoundExecutor(Sound.MOB_AXOLOTL_IDLE), all(
                        RandomSoundEvaluator(),
                        IBehaviorEvaluator { entity: EntityMob? -> !isInsideOfWater }), 6, 1
                ),
                Behavior(
                    MeleeAttackExecutor(
                        CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET,
                        0.3f,
                        17,
                        true,
                        30
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET), 5, 1
                ),
                Behavior(
                    FlatRandomRoamExecutor(0.4f, 12, 40, true, 100, true, 10),
                    PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_BE_ATTACKED_TIME, 0, 100),
                    4,
                    1
                ),
                Behavior(
                    EntityBreedingExecutor<EntityAxolotl>(EntityAxolotl::class.java, 16, 100, 0.5f),
                    { entity: EntityMob -> entity.memoryStorage!!.get<Boolean>(CoreMemoryTypes.Companion.IS_IN_LOVE) },
                    3,
                    1
                ),
                Behavior(
                    MoveToTargetExecutor(CoreMemoryTypes.Companion.NEAREST_FEEDING_PLAYER, 0.4f, true),
                    MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.NEAREST_FEEDING_PLAYER),
                    2,
                    1
                ),
                Behavior(
                    LookAtTargetExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 100),
                    ProbabilityEvaluator(4, 10),
                    1,
                    1,
                    100
                ),
                Behavior(
                    FlatRandomRoamExecutor(0.2f, 12, 100, false, -1, false, 10),
                    { entity: EntityMob -> !entity.isInsideOfWater }, 1, 1
                ),
                Behavior(
                    SpaceRandomRoamExecutor(0.36f, 12, 1, 80, false, -1, false, 10),
                    { EntityMob: EntityMob -> EntityMob.isInsideOfWater }, 1, 1
                )
            ),
            Set.of<ISensor>(
                NearestFeedingPlayerSensor(8.0, 0.0),
                NearestPlayerSensor(8.0, 0.0, 20),
                NearestTargetEntitySensor<Entity>(
                    0.0, 16.0, 20,
                    List.of<MemoryType<Entity?>?>(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                    Function<Entity, Boolean> { entity: Entity? ->
                        this.attackTarget(
                            entity!!
                        )
                    }),
                BlockSensor(BlockFlowingWater::class.java, CoreMemoryTypes.Companion.NEAREST_BLOCK, 16, 5, 10),
                ISensor { entity: EntityMob? ->
                    if (level!!.tick % 20 == 0) {
                        val lastAttack = memoryStorage.get<Entity>(CoreMemoryTypes.Companion.LAST_ATTACK_ENTITY)
                        if (lastAttack != null) {
                            if (!lastAttack.isAlive) {
                                if (lastAttack is EntityMob) {
                                    if (lastAttack.getLastDamageCause() is EntityDamageByEntityEvent) {
                                        if (event.getDamager() is Player) {
                                            player.removeEffect(EffectType.Companion.MINING_FATIGUE)
                                            player.addEffect(
                                                Effect.Companion.get(EffectType.Companion.REGENERATION).setDuration(
                                                    (if (player.hasEffect(
                                                            EffectType.Companion.REGENERATION
                                                        )
                                                    ) player.getEffect(EffectType.Companion.REGENERATION)
                                                        .getDuration() else 0) + 100
                                                )
                                            )
                                        }
                                    }
                                }
                                memoryStorage.clear(CoreMemoryTypes.Companion.LAST_ATTACK_ENTITY)
                            }
                        }
                    }
                }
            ),
            Set.of<IController>(
                LookController(true, true),
                ConditionalController(
                    Pair.of<Predicate<EntityMob>, IController>(
                        Predicate<EntityMob> { obj: EntityMob -> obj.isInsideOfWater },
                        DiveController()
                    ), Pair.of<Predicate<EntityMob>, IController>(
                        Predicate<EntityMob> { obj: EntityMob -> obj.isInsideOfWater }, SpaceMoveController()
                    ), Pair.of<Predicate<EntityMob>, IController>(
                        Predicate<EntityMob> { entity: EntityMob -> !entity.isInsideOfWater }, WalkController()
                    ), Pair.of<Predicate<EntityMob>, IController>(
                        Predicate<EntityMob> { entity: EntityMob -> !entity.isInsideOfWater }, FluctuateController()
                    )
                )
            ),
            ConditionalAStarRouteFinder(
                this,
                Pair.of<Predicate<EntityMob>, IRouteFinder>(
                    Predicate<EntityMob> { ent: EntityMob -> !ent.isInsideOfWater }, SimpleFlatAStarRouteFinder(
                        WalkingPosEvaluator(),
                        this
                    )
                ),
                Pair.of<Predicate<EntityMob>, IRouteFinder>(
                    Predicate<EntityMob> { obj: EntityMob -> obj.isInsideOfWater }, SimpleSpaceAStarRouteFinder(
                        SwimmingPosEvaluator(),
                        this
                    )
                )
            ),
            this
        )
    }

    override fun onInteract(player: Player, item: Item, clickedPos: Vector3): Boolean {
        if (item.id == Item.WATER_BUCKET) {
            val bucket = Item.get(Item.AXOLOTL_BUCKET)
            val tag = CompoundTag()
            tag.putInt("Variant", getVariant())
            bucket.setCompoundTag(tag)
            player.inventory.setItemInHand(bucket)
            this.close()
        }
        return super.onInteract(player, item, clickedPos)
    }

    override fun getHeight(): Float {
        return 0.42f
    }

    override fun getWidth(): Float {
        return 0.75f
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        if (source.cause == DamageCause.SUFFOCATION && this.locator.levelBlock.canPassThrough()) {
            if (airTicks > -5600 || level!!.isRaining || level!!.isThundering) return false
        }
        return super.attack(source)
    }

    override fun initEntity() {
        this.maxHealth = 14
        super.initEntity()
        if (!hasVariant()) {
            setVariant(randomVariant())
        }
    }

    override fun getOriginalName(): String {
        return "Axolotl"
    }

    override fun isBreedingItem(item: Item): Boolean {
        return item.id == Item.TROPICAL_FISH_BUCKET
    }

    override fun useBreedingItem(player: Player, item: Item): Boolean {
        memoryStorage.put<Player>(CoreMemoryTypes.Companion.LAST_FEED_PLAYER, player)
        memoryStorage.put<Int>(CoreMemoryTypes.Companion.LAST_BE_FEED_TIME, level!!.tick)
        sendBreedingAnimation(item)
        return player.inventory.setItemInHand(Item.get(Item.WATER_BUCKET))
    }

    override fun getAllVariant(): IntArray {
        return VARIANTS
    }

    override fun randomVariant(): Int {
        if (Utils.rand(0, 1200) == 0) return VARIANTS[VARIANTS.size - 1]
        return VARIANTS[Utils.rand(
            0,
            VARIANTS.size - 2
        )]
    }

    override fun getDiffHandDamage(): FloatArray? {
        return DIFF_DAMAGE
    }

    override fun getExperienceDrops(): Int {
        return 1
    }

    override fun attackTarget(entity: Entity): Boolean {
        return when (entity.getIdentifier()) {
            EntityID.Companion.COD, EntityID.Companion.ELDER_GUARDIAN, EntityID.Companion.GLOW_SQUID, EntityID.Companion.GUARDIAN, EntityID.Companion.PUFFERFISH, EntityID.Companion.SALMON, EntityID.Companion.TADPOLE, EntityID.Companion.TROPICALFISH, EntityID.Companion.DROWNED -> true
            else -> false
        }
    }

    companion object {
        private val VARIANTS = intArrayOf(0, 1, 2, 3, 4)

        private val DIFF_DAMAGE = floatArrayOf(2f, 2f, 2f)
    }
}
