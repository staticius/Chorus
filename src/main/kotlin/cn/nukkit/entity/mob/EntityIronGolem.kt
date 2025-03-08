/*
 * https://PowerNukkit.org - The Nukkit you know but Powerful!
 * Copyright (C) 2020  José Roberto de Araújo Júnior
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
import cn.nukkit.entity.ai.executor.MeleeAttackExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestEntitySensor
import cn.nukkit.entity.mob.monster.EntityMonster
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.item.*
import cn.nukkit.level.GameRule
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.level.particle.DestroyBlockParticle
import cn.nukkit.level.vibration.VibrationEvent
import cn.nukkit.level.vibration.VibrationType
import cn.nukkit.math.*
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.FloatTag
import cn.nukkit.nbt.tag.ListTag
import java.util.Set
import java.util.concurrent.*

/**
 * @author joserobjr
 * @since 2021-01-13
 */
class EntityIronGolem(chunk: IChunk?, nbt: CompoundTag) : EntityGolem(chunk, nbt), EntityOwnable {
    override fun getIdentifier(): String {
        return EntityID.Companion.IRON_GOLEM
    }

    private var attackingPlayer: Boolean = false

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            Set.of<IBehavior>(
                Behavior(
                    MeleeAttackExecutor(CoreMemoryTypes.Companion.ATTACK_TARGET, 0.2f, 40, true, 30), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.ATTACK_TARGET),
                        not(
                            any(
                                IBehaviorEvaluator { entity: EntityMob -> entity.getServer()!!.getDifficulty() == 0 },
                                all(
                                    IBehaviorEvaluator { entity: EntityMob? ->
                                        attackingPlayer =
                                            getMemoryStorage().get<Entity>(CoreMemoryTypes.Companion.ATTACK_TARGET) is Player
                                    },
                                    IBehaviorEvaluator { entity: EntityMob? -> hasOwner(false) }
                                )))
                    ), 3, 1),
                Behavior(
                    MeleeAttackExecutor(CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY, 0.2f, 40, true, 30), all(
                        EntityCheckEvaluator(CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY),
                        IBehaviorEvaluator { entity: EntityMob? ->
                            attackTarget(
                                getMemoryStorage().get<Entity>(
                                    CoreMemoryTypes.Companion.NEAREST_SHARED_ENTITY
                                )
                            )
                        },
                        not(IBehaviorEvaluator { entity: EntityMob? -> attackingPlayer = false })
                    ), 2, 1
                ),
                Behavior(FlatRandomRoamExecutor(0.2f, 12, 100, false, -1, true, 10), none(), 1, 1)
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

    override fun getOriginalName(): String {
        return "Iron Golem"
    }

    override fun getWidth(): Float {
        return 1.4f
    }

    override fun getHeight(): Float {
        return 2.9f
    }

    override fun initEntity() {
        this.setMaxHealth(100)
        super.initEntity()
        this.syncAttribute(getHealthAttribute())
    }

    override fun spawnTo(player: Player) {
        super.spawnTo(player)
        this.syncAttribute(getHealthAttribute())
    }

    override fun onInteract(player: Player, item: Item, clickedPos: Vector3): Boolean {
        if (item is ItemIronIngot && getHealth() <= getMaxHealth() * 0.75f) {
            level!!.addSound(this.position, Sound.MOB_IRONGOLEM_REPAIR)
            if (player.gamemode != Player.CREATIVE) player.getInventory().getItemInHand().decrement(1)
            heal(25f)
        }
        return super.onInteract(player, item)
    }

    override fun getDrops(): Array<Item?> {
        // Item drops
        val random: ThreadLocalRandom = ThreadLocalRandom.current()
        val flowerAmount: Int = random.nextInt(3)
        val drops: Array<Item?>
        if (flowerAmount > 0) {
            drops = arrayOfNulls(2)
            drops.get(1) = Item.get(BlockID.POPPY, 0, flowerAmount)
        } else {
            drops = arrayOfNulls(1)
        }
        drops.get(0) = Item.get(ItemID.IRON_INGOT, 0, random.nextInt(3, 6))
        return drops
    }


    private fun getHealthAttribute(): Attribute {
        return Attribute.Companion.getAttribute(Attribute.Companion.MAX_HEALTH)!!.setMaxValue(getMaxHealth().toFloat())
            .setValue(if (this.health < getMaxHealth()) this.health else getMaxHealth().toFloat())
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        val health: Float = getHealth()
        if (!super.attack(source)) return false
        for (i: Int in intArrayOf(74, 50, 25)) {
            if (health > i && getHealth() <= i) {
                level!!.addSound(this.position, Sound.MOB_IRONGOLEM_CRACK)
            }
        }
        return true
    }

    override fun setHealth(health: Float) {
        super.setHealth(health)
        syncAttribute(getHealthAttribute())
    }

    override fun getDiffHandDamage(difficulty: Int): Float {
        if (attackingPlayer) {
            when (getServer()!!.getDifficulty()) {
                1 -> return ThreadLocalRandom.current().nextFloat(4.5f, 11.5f)
                2 -> return ThreadLocalRandom.current().nextFloat(7.5f, 21.5f)
                3 -> return ThreadLocalRandom.current().nextFloat(11.5f, 32.25f)
            }
        }
        return ThreadLocalRandom.current().nextFloat(7.5f, 11.75f)
    }

    companion object {
        @JvmStatic
        fun checkAndSpawnGolem(block: Block, player: Player?) {
            if (block.level.gameRules.getBoolean(GameRule.DO_MOB_SPAWNING)) {
                if (block is BlockPumpkin) {
                    faces@ for (blockFace: BlockFace? in BlockFace.entries) {
                        for (i in 1..2) {
                            if (block.getSide(blockFace, i) !is BlockIronBlock) {
                                continue@faces
                            }
                        }
                        faces1@ for (face: BlockFace? in Set.of<BlockFace>(
                            BlockFace.UP,
                            BlockFace.NORTH,
                            BlockFace.EAST
                        )) {
                            for (i in -1..1) {
                                if (block.getSide(blockFace).getSide(face, i) !is BlockIronBlock) {
                                    continue@faces1
                                }
                            }
                            for (i in 0..2) {
                                val location: Block = block.getSide(blockFace, i)
                                block.level.setBlock(location.position, Block.get(Block.AIR))
                                block.level.addParticle(
                                    DestroyBlockParticle(
                                        location.position.add(0.5, 0.5, 0.5),
                                        block
                                    )
                                )
                                block.level.vibrationManager.callVibrationEvent(
                                    VibrationEvent(
                                        null,
                                        location.position.add(0.5, 0.5, 0.5),
                                        VibrationType.BLOCK_DESTROY
                                    )
                                )
                            }
                            for (i in -1..1) {
                                val location: Block = block.getSide(blockFace).getSide(face, i)
                                block.level.setBlock(location.position, Block.get(Block.AIR))
                                block.level.addParticle(
                                    DestroyBlockParticle(
                                        location.position.add(0.5, 0.5, 0.5),
                                        block
                                    )
                                )
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

                            val irongolem: Entity? = Entity.Companion.createEntity(
                                EntityID.Companion.IRON_GOLEM,
                                block.level.getChunk(block.position.getChunkX(), block.position.getChunkZ()),
                                nbt
                            )
                            irongolem!!.spawnToAll()
                            if (irongolem is EntityIronGolem) {
                                if (player != null) {
                                    irongolem.setOwnerName(player.getName())
                                }
                            }
                            return
                        }
                    }
                }
            }
        }
    }
}
