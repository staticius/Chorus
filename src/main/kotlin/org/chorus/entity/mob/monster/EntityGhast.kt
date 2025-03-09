package org.chorus.entity.mob.monster

import cn.nukkit.entity.*
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.EntityCheckEvaluator
import cn.nukkit.entity.ai.evaluator.RandomSoundEvaluator
import cn.nukkit.entity.ai.executor.GhastShootExecutor
import cn.nukkit.entity.ai.executor.PlaySoundExecutor
import cn.nukkit.entity.ai.executor.SpaceRandomRoamExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.route.finder.impl.SimpleSpaceAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.FlyingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestPlayerSensor
import cn.nukkit.entity.projectile.EntityFireball
import cn.nukkit.item.*
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.utils.*
import java.util.*
import java.util.Set

/**
 * @author PikyCZ
 */
class EntityGhast(chunk: IChunk?, nbt: CompoundTag) : EntityMonster(chunk, nbt), EntityFlyable {
    override fun getIdentifier(): String {
        return EntityID.Companion.GHAST
    }

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            Set.of<IBehavior>(
                Behavior(PlaySoundExecutor(Sound.MOB_GHAST_MOAN), RandomSoundEvaluator(), 2, 1),
                Behavior(SpaceRandomRoamExecutor(0.15f, 12, 100, 20, false, -1, true, 10), none(), 1, 1)
            ),
            Set.of<IBehavior>(
                Behavior(
                    GhastShootExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.3f, 64, true, 60, 10),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                    2,
                    1
                ),
                Behavior(
                    GhastShootExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.3f, 28, true, 60, 10),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                    1,
                    1
                )
            ),
            Set.of<ISensor>(NearestPlayerSensor(64.0, 0.0, 20)),
            Set.of<IController>(SpaceMoveController(), LookController(true, true), LiftController()),
            SimpleSpaceAStarRouteFinder(FlyingPosEvaluator(), this),
            this
        )
    }

    override fun initEntity() {
        this.maxHealth = 10
        super.initEntity()
    }

    override fun kill() {
        Arrays.stream(level!!.entities).filter { entity: Entity ->
            if (entity is EntityFireball) {
                return@filter entity.shootingEntity === this && !entity.closed
            }
            false
        }.forEach { obj: Entity -> obj.close() }
        super.kill()
    }

    override fun getWidth(): Float {
        return 4f
    }

    override fun getHeight(): Float {
        return 4f
    }

    override fun getOriginalName(): String {
        return "Ghast"
    }

    override fun getDrops(): Array<Item?> {
        return arrayOf(
            Item.get(Item.GHAST_TEAR, 0, Utils.rand(0, 1)),
            Item.get(Item.GUNPOWDER, 0, Utils.rand(0, 2))
        )
    }

    override fun getExperienceDrops(): Int {
        return 5
    }
}
