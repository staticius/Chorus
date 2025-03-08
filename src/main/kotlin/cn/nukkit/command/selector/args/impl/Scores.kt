package cn.nukkit.command.selector.args.impl

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.command.CommandSender
import cn.nukkit.command.exceptions.SelectorSyntaxException
import cn.nukkit.entity.Entity
import cn.nukkit.level.Transform
import cn.nukkit.scoreboard.scorer.EntityScorer
import cn.nukkit.scoreboard.scorer.PlayerScorer
import cn.nukkit.utils.StringUtils
import java.util.function.Predicate

class Scores : CachedSimpleSelectorArgument() {
    @Throws(SelectorSyntaxException::class)
    override fun cache(
        selectorType: SelectorType?,
        sender: CommandSender?,
        basePos: Transform?,
        vararg arguments: String
    ): Predicate<Entity> {
        ParseUtils.singleArgument(arguments, keyName)
        val conditions = ArrayList<ScoreCondition>()
        for (entry in StringUtils.fastSplit(SCORE_SEPARATOR, arguments[0].substring(1, arguments[0].length - 1))) {
            if (entry.isEmpty()) throw SelectorSyntaxException("Empty score entry is not allowed in selector!")
            val splittedEntry = StringUtils.fastSplit(SCORE_JOINER, entry, 2)
            val objectiveName = splittedEntry[0]
            var condition = splittedEntry[1]
            val reversed: Boolean = ParseUtils.checkReversed(condition)
            if (reversed) condition = condition.substring(1)
            if (condition.contains("..")) {
                //条件为一个区间
                var min = Int.MIN_VALUE
                var max = Int.MAX_VALUE
                val splittedScoreScope = StringUtils.fastSplit(SCORE_SCOPE_SEPARATOR, condition)
                val min_str = splittedScoreScope[0]
                if (!min_str.isEmpty()) {
                    min = min_str.toInt()
                }
                val max_str = splittedScoreScope[1]
                if (!max_str.isEmpty()) {
                    max = max_str.toInt()
                }
                conditions.add(ScoreCondition(objectiveName, min, max, reversed))
            } else {
                //条件为单个数字
                val score = condition.toInt()
                conditions.add(ScoreCondition(objectiveName, score, score, reversed))
            }
        }
        return Predicate { entity: Entity ->
            conditions.stream().allMatch { condition: ScoreCondition -> condition.test(entity) }
        }
    }

    val keyName: String
        get() = "scores"

    val priority: Int
        get() = 5

    @JvmRecord
    protected data class ScoreCondition(val objectiveName: String, val min: Int, val max: Int, val reversed: Boolean) {
        fun test(entity: Entity): Boolean {
            val scoreboard =
                Server.getInstance().scoreboardManager.getScoreboard(objectiveName) ?: return false
            val scorer = if (entity is Player) PlayerScorer(entity) else EntityScorer(entity)
            if (!scoreboard.containLine(scorer)) return false
            val value = scoreboard.getLine(scorer)!!.score
            return (value >= min && value <= max) != reversed
        }
    }

    companion object {
        protected const val SCORE_SEPARATOR: String = ","
        protected const val SCORE_JOINER: String = "="
        protected const val SCORE_SCOPE_SEPARATOR: String = ".."
    }
}
