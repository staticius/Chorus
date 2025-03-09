package org.chorus.entity.mob.monster.humanoid_monster

import cn.nukkit.Player
import cn.nukkit.block.*
import cn.nukkit.entity.EntityID
import cn.nukkit.entity.EntitySmite
import cn.nukkit.entity.EntityWalkable
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.EntityCheckEvaluator
import cn.nukkit.entity.ai.evaluator.IBehaviorEvaluator
import cn.nukkit.entity.ai.evaluator.MemoryCheckNotEmptyEvaluator
import cn.nukkit.entity.ai.evaluator.RandomSoundEvaluator
import cn.nukkit.entity.ai.executor.*
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.MemorizedBlockSensor
import cn.nukkit.entity.ai.sensor.NearestEntitySensor
import cn.nukkit.entity.ai.sensor.NearestPlayerSensor
import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.entity.effect.*
import cn.nukkit.entity.mob.EntityGolem
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.mob.villagers.EntityVillagerV2
import cn.nukkit.item.*
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.math.*
import cn.nukkit.nbt.tag.CompoundTag
import java.util.Set
import java.util.function.Consumer

class EntityZombieVillager(chunk: IChunk?, nbt: CompoundTag?) : EntityZombie(chunk, nbt), EntityWalkable, EntitySmite {
    override fun getIdentifier(): String {
        return EntityID.Companion.ZOMBIE_VILLAGER
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
                    PlaySoundExecutor(
                        Sound.MOB_ZOMBIE_VILLAGER_SAY,
                        if (isBaby()) 1.3f else 0.8f,
                        if (isBaby()) 1.7f else 1.2f,
                        1f,
                        1f
                    ), RandomSoundEvaluator(), 7, 1
                ),
                Behavior(
                    JumpExecutor(), all(
                        IBehaviorEvaluator { entity: EntityMob? -> !memoryStorage!!.isEmpty(CoreMemoryTypes.Companion.NEAREST_BLOCK) },
                        IBehaviorEvaluator { entity: EntityMob ->
                            entity.getCollisionBlocks()!!.stream().anyMatch { block: Block? -> block is BlockTurtleEgg }
                        }), 6, 1, 10
                ),
                Behavior(
                    MoveToTargetExecutor(CoreMemoryTypes.Companion.NEAREST_BLOCK, 0.3f, true),
                    MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.NEAREST_BLOCK),
                    5,
                    1
                ),
                Behavior(
                    MeleeAttackExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.3f, 40, true, 30),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                    4,
                    1
                ),
                Behavior(
                    MeleeAttackExecutor(CoreMemoryTypes.Companion.NEAREST_GOLEM, 0.3f, 40, true, 30),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_GOLEM),
                    3,
                    1
                ),
                Behavior(
                    MeleeAttackExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 0.3f, 40, false, 30),
                    EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_PLAYER),
                    2,
                    1
                ),
                Behavior(FlatRandomRoamExecutor(0.3f, 12, 100, false, -1, true, 10), none(), 1, 1)
            ),
            Set.of<ISensor>(
                NearestPlayerSensor(40.0, 0.0, 0),
                NearestEntitySensor(EntityGolem::class.java, CoreMemoryTypes.Companion.NEAREST_GOLEM, 42.0, 0.0),
                MemorizedBlockSensor(11, 5, 20)
            ),
            Set.of<IController>(WalkController(), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun onInteract(player: Player, item: Item, v: Vector3): Boolean {
        if (item is ItemGoldenApple) {
            if (hasEffect(EffectType.Companion.WEAKNESS)) {
                if (!getDataFlag(EntityFlag.SHAKING)) {
                    setDataFlag(EntityFlag.SHAKING)
                    if (!player.isCreative) {
                        namedTag!!.putString("purifyPlayer", player.loginChainData.xuid)
                        player.inventory.decreaseCount(player.inventory.heldItemIndex)
                    }
                    level!!.addSound(this.position, Sound.MOB_ZOMBIE_REMEDY)
                }
            }
        }
        return false
    }

    private var curingTick = 0

    override fun onUpdate(currentTick: Int): Boolean {
        if (getDataFlag(EntityFlag.SHAKING)) {
            if (curingTick < 2000) {
                curingTick++
            } else transformVillager()
        }
        return super.onUpdate(currentTick)
    }

    override fun initEntity() {
        this.maxHealth = 20
        this.diffHandDamage = floatArrayOf(2.5f, 3f, 4.5f)
        super.initEntity()
        memoryStorage!!.put<Class<out Block>>(
            CoreMemoryTypes.Companion.LOOKING_BLOCK,
            BlockTurtleEgg::class.java
        )
    }

    override fun getOriginalName(): String {
        return "Zombie Villager"
    }

    protected fun transformVillager() {
        this.close()
        equipment.contents.values.forEach(Consumer { i: Item? -> level!!.dropItem(this.position, i) })
        val villager = EntityVillagerV2(this.locator.chunk, this.namedTag)
        villager.addEffect(Effect.Companion.get(EffectType.Companion.NAUSEA).setDuration(200))
        villager.setPosition(this.position)
        villager.setRotation(rotation.yaw, rotation.pitch)
        villager.spawnToAll()
        villager.level!!.addSound(villager.position, Sound.MOB_ZOMBIE_UNFECT)
    }
}
