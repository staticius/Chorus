package org.chorus.entity.mob.animal

import it.unimi.dsi.fastutil.Pair
import org.chorus.Player
import org.chorus.block.BlockFlowingWater
import org.chorus.entity.*
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.*
import org.chorus.entity.ai.evaluator.*
import org.chorus.entity.ai.executor.*
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.route.finder.IRouteFinder
import org.chorus.entity.ai.route.finder.impl.ConditionalAStarRouteFinder
import org.chorus.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import org.chorus.entity.ai.route.finder.impl.SimpleSpaceAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.SwimmingPosEvaluator
import org.chorus.entity.ai.route.posevaluator.WalkingPosEvaluator
import org.chorus.entity.ai.sensor.*
import org.chorus.entity.effect.Effect
import org.chorus.entity.effect.EffectType
import org.chorus.entity.mob.EntityMob
import org.chorus.event.entity.EntityDamageByEntityEvent
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.event.entity.EntityDamageEvent.DamageCause
import org.chorus.item.Item
import org.chorus.item.ItemID
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.math.Vector3
import org.chorus.nbt.tag.CompoundTag
import org.chorus.utils.Utils
import java.util.function.Function
import java.util.function.Predicate

class EntityAxolotl(chunk: IChunk?, nbt: CompoundTag) : EntityAnimal(chunk, nbt), EntitySwimmable, EntityVariant,
    EntityCanAttack {
    override fun getEntityIdentifier(): String {
        return EntityID.AXOLOTL
    }

    override var variant: Int
        get() = super<EntityVariant>.variant
        set(value) { super<EntityVariant>.variant = value }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>( //用于刷新InLove状态的核心行为
                Behavior(
                    InLoveExecutor(400),
                    all(
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_BE_FEED_TIME, 0, 400),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_IN_LOVE_TIME, 6000, Int.MAX_VALUE)
                    ),
                    1, 1
                ),
                Behavior(
                    object : IBehaviorExecutor {
                        override fun execute(entity: EntityMob): Boolean {
                            moveTarget = memoryStorage[CoreMemoryTypes.Companion.NEAREST_BLOCK]?.position
                            return true
                        }
                    }, all(
                        MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.NEAREST_BLOCK),
                        IBehaviorEvaluator { entity: EntityMob? -> !isInsideOfWater() },
                        not(DistanceEvaluator(CoreMemoryTypes.Companion.NEAREST_BLOCK, 9.0))
                    ), 1, 1
                )
            ),
            setOf<IBehavior>(
                Behavior(
                    PlaySoundExecutor(Sound.MOB_AXOLOTL_SPLASH), all(
                        IBehaviorEvaluator { entity: EntityMob? -> getAirTicks() == 399 }
                    ), 7, 1),
                Behavior(
                    PlaySoundExecutor(Sound.MOB_AXOLOTL_IDLE_WATER), all(
                        RandomSoundEvaluator(),
                        IBehaviorEvaluator { entity: EntityMob? -> isInsideOfWater() }), 7, 1
                ),
                Behavior(
                    PlaySoundExecutor(Sound.MOB_AXOLOTL_IDLE), all(
                        RandomSoundEvaluator(),
                        IBehaviorEvaluator { entity: EntityMob? -> !isInsideOfWater() }), 6, 1
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
                    { entity: EntityMob -> entity.memoryStorage.get<Boolean>(CoreMemoryTypes.Companion.IS_IN_LOVE) },
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
                    { entity: EntityMob -> !entity.isInsideOfWater() }, 1, 1
                ),
                Behavior(
                    SpaceRandomRoamExecutor(0.36f, 12, 1, 80, false, -1, false, 10),
                    { EntityMob: EntityMob -> EntityMob.isInsideOfWater() }, 1, 1
                )
            ),
            setOf<ISensor>(
                NearestFeedingPlayerSensor(8.0, 0.0),
                NearestPlayerSensor(8.0, 0.0, 20),
                NearestTargetEntitySensor<Entity>(
                    0.0, 16.0, 20,
                    listOf(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                    Function<Entity, Boolean> { entity: Entity? ->
                        this.attackTarget(
                            entity!!
                        )
                    }),
                BlockSensor(BlockFlowingWater::class.java, CoreMemoryTypes.Companion.NEAREST_BLOCK, 16, 5, 10),
                object : ISensor {
                    override fun sense(entity: EntityMob) {
                        if (level!!.tick % 20 == 0) {
                            val lastAttack = memoryStorage.get<Entity>(CoreMemoryTypes.Companion.LAST_ATTACK_ENTITY)
                            if (lastAttack != null) {
                                if (!lastAttack.isAlive()) {
                                    if (lastAttack is EntityMob) {
                                        val event = lastAttack.getLastDamageCause()
                                        if (event is EntityDamageByEntityEvent) {
                                            if (event.damager is Player) {
                                                val player = event.damager
                                                player.removeEffect(EffectType.MINING_FATIGUE)
                                                player.addEffect(
                                                    Effect.get(EffectType.REGENERATION).setDuration(
                                                        (if (player.hasEffect(EffectType.REGENERATION)) player.getEffect(
                                                            EffectType.REGENERATION
                                                        )!!.getDuration() else 0) + 100
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
                }
            ),
            setOf<IController>(
                LookController(true, true),
                ConditionalController(
                    Pair.of<Predicate<EntityMob>, IController>(
                        Predicate<EntityMob> { obj: EntityMob -> obj.isInsideOfWater() },
                        DiveController()
                    ), Pair.of<Predicate<EntityMob>, IController>(
                        Predicate<EntityMob> { obj: EntityMob -> obj.isInsideOfWater() }, SpaceMoveController()
                    ), Pair.of<Predicate<EntityMob>, IController>(
                        Predicate<EntityMob> { entity: EntityMob -> !entity.isInsideOfWater() }, WalkController()
                    ), Pair.of<Predicate<EntityMob>, IController>(
                        Predicate<EntityMob> { entity: EntityMob -> !entity.isInsideOfWater() }, FluctuateController()
                    )
                )
            ),
            ConditionalAStarRouteFinder(
                this,
                Pair.of<Predicate<EntityMob>, IRouteFinder>(
                    Predicate<EntityMob> { ent: EntityMob -> !ent.isInsideOfWater() }, SimpleFlatAStarRouteFinder(
                        WalkingPosEvaluator(),
                        this
                    )
                ),
                Pair.of<Predicate<EntityMob>, IRouteFinder>(
                    Predicate<EntityMob> { obj: EntityMob -> obj.isInsideOfWater() }, SimpleSpaceAStarRouteFinder(
                        SwimmingPosEvaluator(),
                        this
                    )
                )
            ),
            this
        )
    }

    override fun onInteract(player: Player, item: Item, clickedPos: Vector3): Boolean {
        if (item.id == ItemID.WATER_BUCKET) {
            val bucket = Item.get(ItemID.AXOLOTL_BUCKET)
            val tag = CompoundTag()
            tag.putInt("Variant", variant)
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
            if (getAirTicks() > -5600 || level!!.isRaining || level!!.isThundering()) return false
        }
        return super.attack(source)
    }

    override fun initEntity() {
        this.maxHealth = 14
        super.initEntity()
        if (!hasVariant()) {
            variant = (randomVariant())
        }
    }

    override fun getOriginalName(): String {
        return "Axolotl"
    }

    override fun isBreedingItem(item: Item): Boolean {
        return item.id == ItemID.TROPICAL_FISH_BUCKET
    }

    override fun useBreedingItem(player: Player, item: Item): Boolean {
        memoryStorage[CoreMemoryTypes.Companion.LAST_FEED_PLAYER] = player
        memoryStorage[CoreMemoryTypes.Companion.LAST_BE_FEED_TIME] = level!!.tick
        sendBreedingAnimation(item)
        return player.inventory.setItemInHand(Item.get(ItemID.WATER_BUCKET))
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

    override var diffHandDamage: FloatArray
        get() {
            return DIFF_DAMAGE
        }
        set(value) {
            super<EntityAnimal>.diffHandDamage = value
        }

    override fun getExperienceDrops(): Int {
        return 1
    }

    override fun attackTarget(entity: Entity): Boolean {
        return when (entity.getEntityIdentifier()) {
            EntityID.COD, EntityID.ELDER_GUARDIAN, EntityID.GLOW_SQUID, EntityID.GUARDIAN, EntityID.PUFFERFISH, EntityID.SALMON, EntityID.TADPOLE, EntityID.TROPICALFISH, EntityID.DROWNED -> true
            else -> false
        }
    }

    companion object {
        private val VARIANTS = intArrayOf(0, 1, 2, 3, 4)

        private val DIFF_DAMAGE = floatArrayOf(2f, 2f, 2f)
    }
}
