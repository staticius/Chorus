package cn.nukkit.entity.ai.executor

import cn.nukkit.entity.*
import cn.nukkit.entity.ai.evaluator.*
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.memory.MemoryType
import cn.nukkit.entity.data.EntityDataTypes
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.mob.EntityShulker
import cn.nukkit.entity.projectile.EntityShulkerBullet
import cn.nukkit.level.Sound
import cn.nukkit.math.*
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.FloatTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.utils.*
import lombok.RequiredArgsConstructor

@RequiredArgsConstructor
class ShulkerAttackExecutor : IBehaviorExecutor {
    protected val target: MemoryType<out Entity?>? = null
    private var tick = 0
    private var nextAttack = 0

    override fun execute(entity: EntityMob): Boolean {
        val target = entity.memoryStorage!![target] ?: return false
        tick++
        if (tick > nextAttack) {
            tick = 0
            nextAttack = Utils.rand(20, 110)
            val bulletTransform = entity.transform.clone().add(
                Vector3(
                    target.position.south - entity.position.south,
                    target.position.up - entity.position.up,
                    target.position.west - entity.position.west
                ).normalize()
            ).add(0.0, 0.5, 0.0)
            val nbt = CompoundTag()
                .putList(
                    "Pos", ListTag<FloatTag>()
                        .add(FloatTag(bulletTransform.position.x))
                        .add(FloatTag(bulletTransform.position.y))
                        .add(FloatTag(bulletTransform.position.z))
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


            val bulletEntity: Entity = Entity.Companion.createEntity(
                EntityID.Companion.SHULKER_BULLET,
                entity.level!!.getChunk(entity.position.chunkX, entity.position.chunkZ),
                nbt
            )

            if (bulletEntity is EntityShulkerBullet) {
                bulletEntity.memoryStorage!!
                    .put<Entity>(CoreMemoryTypes.Companion.ATTACK_TARGET, target)
            }
            bulletEntity.spawnToAll()
            entity.level!!.addSound(entity.position, Sound.MOB_SHULKER_SHOOT)
        }
        return AllMatchEvaluator(
            EntityCheckEvaluator(this.target),
            DistanceEvaluator(this.target, 16.0),
            NotMatchEvaluator(PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_BE_ATTACKED_TIME, 0, 60))
        ).evaluate(entity)
    }

    override fun onStart(entity: EntityMob) {
        tick = 0
        nextAttack = 0
        if (entity is EntityShulker) {
            entity.setPeeking(40)
            val target = entity.getMemoryStorage()[target] ?: return
            entity.setDataProperty(EntityDataTypes.Companion.TARGET_EID, target.id)
        }
    }

    override fun onStop(entity: EntityMob) {
        if (entity is EntityShulker) {
            entity.setDataProperty(EntityDataTypes.Companion.TARGET_EID, 0)
            entity.setPeeking(0)
        }
    }

    override fun onInterrupt(entity: EntityMob) {
        onStop(entity)
    }
}
