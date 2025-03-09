package org.chorus.entity.mob.monster

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
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestEntitySensor
import cn.nukkit.entity.ai.sensor.PlayerStaringSensor
import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.projectile.EntityProjectile
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause
import cn.nukkit.item.*
import cn.nukkit.level.GameRule
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.utils.*
import java.util.Set

class EntityEnderman(chunk: IChunk?, nbt: CompoundTag) : EntityMonster(chunk, nbt), EntityWalkable {
    override fun getIdentifier(): String {
        return EntityID.Companion.ENDERMAN
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

    override fun getDrops(): Array<Item?> {
        return arrayOf(
            Item.get(Item.ENDER_PEARL, 0, Utils.rand(0, 1)),
            itemInHand
        )
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        if (source.cause == DamageCause.PROJECTILE) {
            if (source is EntityDamageByEntityEvent) {
                if (source.damager is EntityProjectile) {
                    memoryStorage!!.put<Entity>(CoreMemoryTypes.Companion.ATTACK_TARGET, projectile.shootingEntity)
                }
            }
            TeleportExecutor(16, 5, 16).execute(this)
            source.setCancelled()
            return false
        }
        return super.attack(source)
    }
}
