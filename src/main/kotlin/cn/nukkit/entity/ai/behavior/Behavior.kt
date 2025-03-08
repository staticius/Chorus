package cn.nukkit.entity.ai.behavior

import cn.nukkit.entity.ai.evaluator.IBehaviorEvaluator
import cn.nukkit.entity.ai.executor.IBehaviorExecutor
import cn.nukkit.entity.mob.EntityMob
import lombok.Getter

/**
 * 单个的行为对象，包含一个执行器和一个评估器，行为对象委托了它们的方法
 *
 *
 * A single behavior object, containing an executor and an evaluator, with the behavior object delegating their methods
 */
@Getter
class Behavior @JvmOverloads constructor(
    protected var executor: IBehaviorExecutor,
    protected var evaluator: IBehaviorEvaluator,
    override val priority: Int = 1,
    override val weight: Int = 1,
    override val period: Int = 1,
    protected var reevaluate: Boolean = true
) :
    AbstractBehavior() {
    override fun evaluate(entity: EntityMob): Boolean {
        return evaluator.evaluate(entity)
    }

    override fun execute(entity: EntityMob?): Boolean {
        return executor.execute(entity)
    }

    override fun onStart(entity: EntityMob) {
        executor.onStart(entity)
    }

    override fun onInterrupt(entity: EntityMob?) {
        executor.onInterrupt(entity)
    }

    override fun onStop(entity: EntityMob?) {
        executor.onStop(entity)
    }

    override fun toString(): String {
        return "[" + priority + "] " + executor.javaClass.simpleName + " | " + evaluator.javaClass.simpleName
    }
}
