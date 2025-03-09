package cn.nukkit.entity.ai.executor.villager

import cn.nukkit.block.*
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.executor.EntityControl
import cn.nukkit.entity.ai.executor.IBehaviorExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.memory.MemoryType
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.mob.villagers.EntityVillagerV2
import cn.nukkit.item.*
import cn.nukkit.math.*
import cn.nukkit.utils.*
import lombok.RequiredArgsConstructor
import java.util.*

@RequiredArgsConstructor
class GossipExecutor : EntityControl, IBehaviorExecutor {
    protected var tick: Int = 0
    protected var spread: Boolean = false
    private val type: MemoryType<out EntityVillagerV2?>? = null

    override fun execute(entity: EntityMob): Boolean {
        val entity1 = entity.memoryStorage!![type]
        if (entity1 != null) {
            if (entity1.position.toHorizontal().distance(entity.position.toHorizontal()) < 2) {
                if (!spread) {
                    removeRouteTarget(entity)
                    if (entity is EntityVillagerV2) {
                        entity.spreadGossip()
                        spread = true
                    }
                }
                if (entity is EntityVillagerV2) {
                    if (entity1.isHungry && entity.shouldShareFood()) {
                        for (i in 0..<entity.inventory.size) {
                            var item = entity.inventory.getUnclonedItem(i)
                            item.setCount(item.getCount() / 2)
                            if (item.id === Block.WHEAT) item = Item.get(Block.WHEAT, 0, item.getCount() / 3)
                            entity.level!!.dropItem(
                                entity.getLocator().position.add(
                                    0.0,
                                    entity.getEyeHeight().toDouble(),
                                    0.0
                                ),
                                item,
                                Vector3(
                                    entity1.position.x - entity.position.x,
                                    entity1.position.y - entity.position.y,
                                    entity1.position.z - entity.position.z
                                ).normalize().multiply(0.4)
                            )
                        }
                    }
                }
            }
            if (tick % 100 == 0) {
                if (Utils.rand(0, 10) == 0) {
                    Arrays.stream(
                        entity.level!!.getCollidingEntities(
                            entity.getBoundingBox()!!.grow(2.0, 0.0, 2.0)
                        )
                    ).filter { entity2: Entity -> entity2 is EntityVillagerV2 && entity2 !== entity }
                        .map { entity2: Entity -> (entity2 as EntityVillagerV2) }.forEach { entity2: EntityVillagerV2 ->
                            entity2.lookTarget =
                                entity.position
                        }
                }
            }
        }
        return true
    }

    override fun onStart(entity: EntityMob) {
        entity.moveTarget = entity.memoryStorage!![type].position
        entity.lookTarget = entity.memoryStorage!![type].position
        this.tick = 0
        this.spread = false
        entity.memoryStorage!!.put<Int>(CoreMemoryTypes.Companion.LAST_GOSSIP, entity.level!!.tick)
    }

    override fun onStop(entity: EntityMob) {
        entity.memoryStorage!!.clear(type)
    }

    override fun onInterrupt(entity: EntityMob) {
        onStop(entity)
    }

    companion object {
        private const val STAY_TICKS = 60
    }
}
