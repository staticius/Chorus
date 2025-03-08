package cn.nukkit.entity.ai.evaluator

import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.math.Vector3

class BlockCheckEvaluator(protected var blockId: String, protected var offsetVec: Vector3) : IBehaviorEvaluator {
    override fun evaluate(entity: EntityMob): Boolean {
        return entity.level!!.getTickCachedBlock(entity.position.add(offsetVec)).id == blockId
    }
}
