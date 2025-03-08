package cn.nukkit.entity.ai.evaluator

/**
 * 一个抽象类代表着这个评估器会评估多个行为;
 *
 *
 * An abstract class represents multiple behaviors that this evaluator will evaluate.
 */
abstract class MultiBehaviorEvaluator : IBehaviorEvaluator {
    protected var evaluators: Set<IBehaviorEvaluator?>

    constructor(evaluators: Set<IBehaviorEvaluator?>) {
        this.evaluators = evaluators
    }

    constructor(vararg evaluators: IBehaviorEvaluator?) {
        this.evaluators = java.util.Set.of(*evaluators)
    }
}
