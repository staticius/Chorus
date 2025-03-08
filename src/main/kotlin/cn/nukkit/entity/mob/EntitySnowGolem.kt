package cn.nukkit.entity.mob

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
import cn.nukkit.entity.ai.executor.FlatRandomRoamExecutor
import cn.nukkit.entity.ai.executor.SnowGolemShootExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestEntitySensor
import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.entity.mob.monster.EntityMonster
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause
import cn.nukkit.item.*
import cn.nukkit.level.GameRule
import cn.nukkit.level.format.IChunk
import cn.nukkit.level.particle.DestroyBlockParticle
import cn.nukkit.level.vibration.VibrationEvent
import cn.nukkit.level.vibration.VibrationType
import cn.nukkit.math.*
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.FloatTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.network.protocol.LevelSoundEventPacket
import cn.nukkit.registry.Registries
import java.util.Set

class EntitySnowGolem(chunk: IChunk?, nbt: CompoundTag) : EntityGolem(chunk, nbt) {
    override fun getIdentifier(): String {
        return EntityID.Companion.SNOW_GOLEM
    }

    var waterTicks: Int = 0

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            Set.of<IBehavior>(
                Behavior(
                    SnowGolemShootExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.4f, 16, true, 20, 0),
                    all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                        IBehaviorEvaluator { entity: EntityMob? -> getMemoryStorage()!!.get<Entity>(CoreMemoryTypes.Companion.ATTACK_TARGET) !is EntitySnowGolem }),
                    3,
                    1
                ),
                Behavior(
                    SnowGolemShootExecutor(CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY, 0.4f, 10, true, 20, 0),
                    all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY),
                        IBehaviorEvaluator { entity: EntityMob? ->
                            attackTarget(
                                getMemoryStorage()!!.get<Entity>(
                                    CoreMemoryTypes.Companion.ATTACK_TARGET
                                )
                            )
                        }
                    ),
                    2,
                    1),
                Behavior(FlatRandomRoamExecutor(0.3f, 12, 100, false, -1, true, 10), none(), 1, 1)
            ),
            Set.of<ISensor>(
                NearestEntitySensor(
                    EntityMonster::class.java,
                    CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY,
                    16.0,
                    0.0
                )
            ),
            Set.of<IController>(WalkController(), LookController(true, true)),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun onInteract(player: Player, item: Item, clickedPos: Vector3): Boolean {
        if (item is ItemShears) {
            if (!isSheared()) {
                this.setSheared(true)
                level!!.addLevelSoundEvent(this.position, LevelSoundEventPacket.SOUND_SHEAR)
                if (player.gamemode != Player.CREATIVE) player.getInventory().getItemInHand()
                    .setDamage(item.getDamage() + 1)
                level!!.dropItem(
                    position.add(
                        0.0,
                        getEyeHeight().toDouble(), 0.0
                    ), Item.get(Block.CARVED_PUMPKIN)
                )
            }
        }
        return super.onInteract(player, item)
    }

    override fun getOriginalName(): String {
        return "Snow Golem"
    }

    override fun getWidth(): Float {
        return 0.4f
    }

    override fun getHeight(): Float {
        return 1.8f
    }

    override fun initEntity() {
        this.setMaxHealth(4)
        setSheared(false)
        super.initEntity()
    }

    fun setSheared(sheared: Boolean) {
        setDataFlag(EntityFlag.SHEARED, sheared)
    }

    fun isSheared(): Boolean {
        return getDataFlag(EntityFlag.SHEARED)
    }

    override fun onUpdate(currentTick: Int): Boolean {
        waterTicks++
        if (level!!.gameRules.getBoolean(GameRule.MOB_GRIEFING)) {
            if (getLocator().getLevelBlock().isAir()) {
                val support: Block = getLocator().getLevelBlock().down()
                if (support.isFullBlock() && !support.isAir()) {
                    level!!.setBlock(getLocator().getLevelBlock().position, Block.get(Block.SNOW_LAYER))
                }
            }
        }
        if (this.waterTicks >= 20) {
            if ((level!!.isRaining() && !this.isUnderBlock()) || getLocator().getLevelBlock() is BlockLiquid || Registries.BIOME.get(
                    level!!.getBiomeId(
                        position.getFloorX(),
                        position.getFloorY(), position.getFloorZ()
                    )
                ).temperature > 1.0
            ) {
                this.attack(EntityDamageEvent(this, DamageCause.WEATHER, 1f))
            }
            this.waterTicks = 0
        }
        return super.onUpdate(currentTick)
    }

    companion object {
        @JvmStatic
        fun checkAndSpawnGolem(block: Block) {
            if (block.level.gameRules.getBoolean(GameRule.DO_MOB_SPAWNING)) {
                if (block is BlockPumpkin) {
                    faces@ for (blockFace: BlockFace? in BlockFace.entries) {
                        for (i in 1..2) {
                            if (block.getSide(blockFace, i) !is BlockSnow) {
                                continue@faces
                            }
                        }
                        for (i in 0..2) {
                            val location: Block = block.getSide(blockFace, i)
                            block.level.setBlock(location.position, Block.get(Block.AIR))
                            block.level.addParticle(DestroyBlockParticle(location.position.add(0.5, 0.5, 0.5), block))
                            block.level.vibrationManager.callVibrationEvent(
                                VibrationEvent(
                                    null,
                                    location.position.add(0.5, 0.5, 0.5),
                                    VibrationType.BLOCK_DESTROY
                                )
                            )
                        }
                        val pos: Block = block.getSide(blockFace, 2)
                        val nbt: CompoundTag = CompoundTag()
                            .putList(
                                "Pos", ListTag<FloatTag>()
                                    .add(FloatTag(pos.position.south + 0.5))
                                    .add(FloatTag(pos.position.up))
                                    .add(FloatTag(pos.position.west + 0.5))
                            )
                            .putList(
                                "Motion", ListTag<FloatTag>()
                                    .add(FloatTag(0f))
                                    .add(FloatTag(0f))
                                    .add(FloatTag(0f))
                            )
                            .putList(
                                "Rotation", ListTag<FloatTag>()
                                    .add(FloatTag(0f))
                                    .add(FloatTag(0f))
                            )

                        val snowgolem: Entity? = Entity.Companion.createEntity(
                            EntityID.Companion.SNOW_GOLEM,
                            block.level.getChunk(block.position.getChunkX(), block.position.getChunkZ()),
                            nbt
                        )
                        snowgolem!!.spawnToAll()
                        return
                    }
                }
            }
        }
    }
}
