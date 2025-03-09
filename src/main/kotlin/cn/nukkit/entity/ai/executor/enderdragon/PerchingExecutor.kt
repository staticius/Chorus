package cn.nukkit.entity.ai.executor.enderdragon

import cn.nukkit.Player
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.executor.EntityControl
import cn.nukkit.entity.ai.executor.IBehaviorExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.effect.*
import cn.nukkit.entity.item.EntityAreaEffectCloud
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.math.*
import cn.nukkit.nbt.tag.*

class PerchingExecutor : EntityControl, IBehaviorExecutor {
    private var stayTick = -1

    override fun execute(entity: EntityMob): Boolean {
        val target = Vector3(0.0, (entity.level!!.getHighestBlockAt(0, 0) + 1).toDouble(), 0.0)
        if (stayTick >= 0) {
            stayTick++
        }
        if (entity.position.distance(target) <= 1) {
            if (stayTick == -1) stayTick = 0
            if (stayTick == 25) {
                entity.viewers.values.stream()
                    .filter { player: Player -> player.position.distance(Vector3(0.0, 64.0, 0.0)) <= 20 }.findAny()
                    .ifPresent { player: Player ->
                        removeRouteTarget(entity)
                        setLookTarget(entity, player.position)
                        val toPlayerVector = Vector3(
                            player.position.x - entity.position.x,
                            player.position.y - entity.position.y,
                            player.position.z - entity.position.z
                        ).normalize()
                        val transform = entity.transform.add(toPlayerVector.multiply(10.0))
                        transform.position.y =
                            (transform.level.getHighestBlockAt(transform.position.toHorizontal()) + 1).toDouble()
                        val areaEffectCloud = Entity.Companion.createEntity(
                            EntityID.Companion.AREA_EFFECT_CLOUD, transform.chunk,
                            CompoundTag().putList(
                                "Pos", ListTag<Tag>()
                                    .add(FloatTag(transform.position.x))
                                    .add(FloatTag(transform.position.y))
                                    .add(FloatTag(transform.position.z))
                            )
                                .putList(
                                    "Rotation", ListTag<Tag>()
                                        .add(FloatTag(0f))
                                        .add(FloatTag(0f))
                                )
                                .putList(
                                    "Motion", ListTag<Tag>()
                                        .add(FloatTag(0f))
                                        .add(FloatTag(0f))
                                        .add(FloatTag(0f))
                                )
                                .putInt("Duration", 60)
                                .putFloat("InitialRadius", 6f)
                                .putFloat("Radius", 6f)
                                .putFloat("Height", 1f)
                                .putFloat("RadiusChangeOnPickup", 0f)
                                .putFloat("RadiusPerTick", 0f)
                        ) as EntityAreaEffectCloud

                        val effects: List<Effect?> =
                            PotionType.Companion.get(PotionType.Companion.HARMING.id)
                                .getEffects(false)
                        for (effect in effects) {
                            if (effect != null && areaEffectCloud != null) {
                                areaEffectCloud.cloudEffects!!.add(effect.setVisible(false).setAmbient(false))
                                areaEffectCloud.spawnToAll()
                            }
                        }
                        areaEffectCloud.spawnToAll()
                    }
            }
        } else {
            setRouteTarget(entity, target)
            setLookTarget(entity, target)
        }
        if (stayTick > 100) {
            return false
        } else if (stayTick >= 0) {
            entity.teleport(target)
        }
        return true
    }


    override fun onStart(entity: EntityMob) {
        val player = entity.memoryStorage!!.get<Player>(CoreMemoryTypes.Companion.NEAREST_PLAYER) ?: return
        setLookTarget(entity, player.position)
        setRouteTarget(entity, player.position)
        stayTick = -1
    }

    override fun onStop(entity: EntityMob) {
        entity.memoryStorage!!.put<Boolean>(CoreMemoryTypes.Companion.FORCE_PERCHING, false)
        entity.isEnablePitch = false
    }

    override fun onInterrupt(entity: EntityMob) {
        onStop(entity)
    }
}
