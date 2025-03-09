package org.chorus.entity.mob.monster.humanoid_monster

import cn.nukkit.Player
import cn.nukkit.block.*
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.*
import cn.nukkit.entity.ai.executor.*
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.memory.MemoryType
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.BlockSensor
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestPlayerSensor
import cn.nukkit.entity.ai.sensor.NearestTargetEntitySensor
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.mob.animal.EntityAxolotl
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.inventory.EntityInventoryHolder
import cn.nukkit.item.*
import cn.nukkit.item.enchantment.Enchantment
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.utils.*
import java.util.List
import java.util.Set
import java.util.concurrent.*
import java.util.function.Function
import kotlin.math.min

class EntityDrowned(chunk: IChunk?, nbt: CompoundTag?) : EntityZombie(chunk, nbt), EntitySwimmable, EntityWalkable,
    EntitySmite {
    override fun getIdentifier(): String {
        return EntityID.Companion.DROWNED
    }

    override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            Set.of<IBehavior>(
                Behavior(
                    NearestBlockIncementExecutor(),
                    { entity: EntityMob? ->
                        !memoryStorage!!.isEmpty(CoreMemoryTypes.Companion.NEAREST_BLOCK) && memoryStorage!!.get<Block>(
                            CoreMemoryTypes.Companion.NEAREST_BLOCK
                        ) is BlockTurtleEgg
                    }, 1, 1
                )
            ),
            Set.of<IBehavior>(
                Behavior(
                    PlaySoundExecutor(Sound.MOB_DROWNED_SAY_WATER), all(
                        RandomSoundEvaluator(),
                        IBehaviorEvaluator { entity: EntityMob? -> isInsideOfWater }), 11, 1
                ),
                Behavior(
                    PlaySoundExecutor(Sound.MOB_DROWNED_SAY), all(
                        RandomSoundEvaluator(),
                        IBehaviorEvaluator { entity: EntityMob? -> !isInsideOfWater }), 10, 1
                ),
                Behavior(
                    JumpExecutor(), all(
                        IBehaviorEvaluator { entity: EntityMob? -> !memoryStorage!!.isEmpty(CoreMemoryTypes.Companion.NEAREST_BLOCK) },
                        IBehaviorEvaluator { entity: EntityMob ->
                            entity.getCollisionBlocks()!!.stream().anyMatch { block: Block? -> block is BlockTurtleEgg }
                        }), 9, 1, 10
                ),
                Behavior(
                    MoveToTargetExecutor(CoreMemoryTypes.Companion.NEAREST_BLOCK, 0.3f, true),
                    MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.NEAREST_BLOCK),
                    8,
                    1
                ),
                Behavior(
                    TridentThrowExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.3f, 15, true, 30, 20), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                        DistanceEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET, 32.0, 3.0),
                        IBehaviorEvaluator { entity: EntityMob? -> getItemInHand().id == Item.TRIDENT }
                    ), 7, 1),
                Behavior(
                    TridentThrowExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.3f, 15, false, 30, 20), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                        DistanceEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER, 32.0, 3.0),
                        IBehaviorEvaluator { entity: EntityMob? -> getItemInHand().id == Item.TRIDENT },
                        any(
                            IBehaviorEvaluator { entity: EntityMob? -> level!!.isNight },
                            IBehaviorEvaluator { entity: EntityMob? ->
                                memoryStorage!!.get<Player?>(CoreMemoryTypes.Companion.NEAREST_PLAYER) != null && memoryStorage!!.get<Player>(
                                    CoreMemoryTypes.Companion.NEAREST_PLAYER
                                ).isInsideOfWater
                            }
                        )
                    ), 6, 1),
                Behavior(
                    TridentThrowExecutor(
                        CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET,
                        0.3f,
                        15,
                        false,
                        30,
                        20
                    ), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                        DistanceEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET, 32.0, 3.0),
                        IBehaviorEvaluator { entity: EntityMob? -> getItemInHand().id == Item.TRIDENT }
                    ), 5, 1),
                Behavior(
                    MeleeAttackExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.3f, 40, true, 30),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                    4,
                    1
                ),
                Behavior(
                    MeleeAttackExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.3f, 40, false, 30), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                        any(
                            IBehaviorEvaluator { entity: EntityMob? -> level!!.isNight },
                            IBehaviorEvaluator { entity: EntityMob? ->
                                memoryStorage!!.get<Player?>(CoreMemoryTypes.Companion.NEAREST_PLAYER) != null && memoryStorage!!.get<Player>(
                                    CoreMemoryTypes.Companion.NEAREST_PLAYER
                                ).isInsideOfWater
                            }
                        )
                    ), 3, 1),
                Behavior(
                    MeleeAttackExecutor(
                        CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET,
                        0.3f,
                        40,
                        true,
                        30
                    ), EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET), 2, 1
                ),
                Behavior(FlatRandomRoamExecutor(0.3f, 12, 100, false, -1, false, 10), none(), 1, 1)
            ),
            Set.of<ISensor>(
                NearestPlayerSensor(64.0, 0.0, 0),
                NearestTargetEntitySensor<Entity>(
                    0.0, 16.0, 20,
                    List.of<MemoryType<Entity?>?>(CoreMemoryTypes.Companion.NEAREST_SUITABLE_ATTACK_TARGET),
                    Function<Entity, Boolean> { entity: Entity? ->
                        this.attackTarget(
                            entity!!
                        )
                    }),
                BlockSensor(BlockTurtleEgg::class.java, CoreMemoryTypes.Companion.NEAREST_BLOCK, 11, 15, 10)
            ),
            Set.of<IController>(WalkController(), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun getFloatingForceFactor(): Double {
        if (any(
                EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.NEAREST_BLOCK)
            ).evaluate(this)
        ) {
            if (hasWaterAt(this.floatingHeight)) {
                return 1.3
            }
            return 0.7
        }
        return 0.0
    }

    override fun initEntity() {
        this.maxHealth = 20
        this.diffHandDamage = floatArrayOf(2.5f, 3f, 4.5f)
        super.initEntity()
        memoryStorage!!.put<Boolean>(CoreMemoryTypes.Companion.ENABLE_DIVE_FORCE, true)
        val random = Utils.rand(0, 10000)
        if (random < 85) {
            setItemInHand(Item.get(Item.FISHING_ROD))
        } else if (random < 1585 && !wasTransformed()) {
            setItemInHand(Item.get(Item.TRIDENT))
        }
        if (Utils.rand(0, 100) < 3) {
            itemInOffhand = Item.get(Item.NAUTILUS_SHELL)
        }
    }

    override fun getOriginalName(): String {
        return "Drowned"
    }

    override fun getDrops(): Array<Item?> {
        var trident = Item.AIR
        if (getItemInHand() is ItemTrident) {
            val event = getLastDamageCause()
            var lootingLevel = 0
            if (event is EntityDamageByEntityEvent) {
                if (event.damager is EntityInventoryHolder) {
                    lootingLevel = holder.getItemInHand().getEnchantmentLevel(Enchantment.ID_LOOTING)
                }
            }
            if (ThreadLocalRandom.current().nextInt(1, 100) < min(37.0, (25 + lootingLevel).toDouble())) {
                trident = Item.get(Item.TRIDENT)
            }
        }
        return arrayOf(
            Item.get(Item.ROTTEN_FLESH),
            trident
        )
    }

    override fun isUndead(): Boolean {
        return true
    }

    override fun isPreventingSleep(player: Player?): Boolean {
        return true
    }

    override fun getExperienceDrops(): Int {
        return if (isBaby()) 7 else 5
    }

    fun wasTransformed(): Boolean {
        if (namedTag!!.contains("Transformed")) {
            return namedTag!!.getBoolean("Transformed")
        }
        return false
    }

    override fun attackTarget(entity: Entity): Boolean {
        return super.attackTarget(entity) || entity is EntityAxolotl
    }

    override fun transform() {
        throw UnsupportedOperationException("Drowned cannot transform")
    }
}
