package org.chorus.entity.mob.monster

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
import org.chorus.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import org.chorus.entity.ai.route.posevaluator.WalkingPosEvaluator
import org.chorus.entity.ai.sensor.ISensor
import org.chorus.entity.ai.sensor.NearestEntitySensor
import org.chorus.entity.ai.sensor.PlayerStaringSensor
import org.chorus.entity.data.EntityFlag
import org.chorus.entity.mob.EntityMob
import org.chorus.entity.projectile.EntityProjectile
import org.chorus.event.entity.EntityDamageByEntityEvent
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.event.entity.EntityDamageEvent.DamageCause
import org.chorus.item.*
import org.chorus.level.GameRule
import org.chorus.level.Sound
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import org.chorus.utils.*
import java.util.Set

class EntityEnderman(chunk: IChunk?, nbt: CompoundTag) : EntityMonster(chunk, nbt), EntityWalkable {
    override fun getIdentifier(): String {
        return EntityID.ENDERMAN
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            Set.of<IBehavior>(
                Behavior(StaringAttackTargetExecutor(), none(), 1, 1, 1, true)
            ),
            Set.of<IBehavior>(
                Behavior(
                    PlaySoundExecutor(Sound.MOB_ENDERMEN_IDLE, 0.8f, 1.2f, 1f, 1f),
                    all(not(EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET)), RandomSoundEvaluator()),
                    6,
                    1,
                    1,
                    true
                ),
                Behavior(
                    PlaySoundExecutor(Sound.MOB_ENDERMEN_SCREAM, 0.8f, 1.2f, 1f, 1f),
                    all(EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET), RandomSoundEvaluator(10, 7)),
                    5,
                    1,
                    1,
                    true
                ),
                Behavior(
                    TeleportExecutor(16, 5, 16), any(
                        all(
                            IBehaviorEvaluator { entity: EntityMob? -> level!!.isRaining },
                            IBehaviorEvaluator { entity: EntityMob? -> !isUnderBlock },
                            IBehaviorEvaluator { entity: EntityMob? -> level!!.tick % 10 == 0 }),
                        IBehaviorEvaluator { entity: EntityMob? -> isInsideOfWater },
                        all(
                            IBehaviorEvaluator { entity: EntityMob? -> memoryStorage!!.isEmpty(CoreMemoryTypes.Companion.ATTACK_TARGET) },
                            IBehaviorEvaluator { entity: EntityMob? -> level!!.tick % 20 == 0 },
                            ProbabilityEvaluator(2, 25)
                        ),
                        all(
                            IBehaviorEvaluator { entity: EntityMob? -> !memoryStorage!!.isEmpty(CoreMemoryTypes.Companion.ATTACK_TARGET) },
                            ProbabilityEvaluator(1, 20),
                            PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_BE_ATTACKED_TIME, 0, 10)
                        )
                    ), 4, 1
                ),
                Behavior(
                    MeleeAttackExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.45f, 64, true, 30), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                        any(
                            IBehaviorEvaluator { entity: EntityMob? ->
                                memoryStorage!!.get<Entity>(CoreMemoryTypes.Companion.ATTACK_TARGET) is Player && holder.getInventory() != null && (holder.getInventory()
                                    .getHelmet().getId() != Block.CARVED_PUMPKIN)
                            },
                            IBehaviorEvaluator { entity: EntityMob? -> memoryStorage!!.get<Entity>(CoreMemoryTypes.Companion.ATTACK_TARGET) is EntityMob }
                        )
                    ), 3, 1),
                Behavior(
                    EndermanBlockExecutor(), all(
                        IBehaviorEvaluator { entity: EntityMob? ->
                            level!!.gameRules.getBoolean(GameRule.MOB_GRIEFING)
                        },
                        IBehaviorEvaluator { entity: EntityMob? -> level!!.tick % 60 == 0 },
                        ProbabilityEvaluator(1, 20)
                    ), 2, 1, 1, true
                ),
                Behavior(FlatRandomRoamExecutor(0.3f, 12, 100, false, -1, true, 10), none(), 1, 1)
            ),
            Set.of<ISensor>(
                PlayerStaringSensor(64.0, 20.0, false),
                NearestEntitySensor(EntityEndermite::class.java, CoreMemoryTypes.Companion.NEAREST_ENDERMITE, 64.0, 0.0)
            ),
            Set.of<IController>(WalkController(), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun initEntity() {
        this.maxHealth = 40
        this.diffHandDamage = floatArrayOf(4f, 7f, 10f)
        super.initEntity()
    }

    override fun getWidth(): Float {
        return 0.6f
    }

    override fun getHeight(): Float {
        return 2.9f
    }

    override fun getOriginalName(): String {
        return "Enderman"
    }

    override fun isPreventingSleep(player: Player?): Boolean {
        return this.getDataFlag(EntityFlag.ANGRY)
    }

    override fun getDrops(): Array<Item> {
        return arrayOf(
            Item.get(ItemID.ENDER_PEARL, 0, Utils.rand(0, 1)),
            itemInHand
        )
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        if (source.cause == DamageCause.PROJECTILE) {
            if (source is EntityDamageByEntityEvent) {
                if (source.damager is EntityProjectile) {
                    memoryStorage!!.set<Entity>(CoreMemoryTypes.Companion.ATTACK_TARGET, projectile.shootingEntity)
                }
            }
            TeleportExecutor(16, 5, 16).execute(this)
            source.setCancelled()
            return false
        }
        return super.attack(source)
    }
}
