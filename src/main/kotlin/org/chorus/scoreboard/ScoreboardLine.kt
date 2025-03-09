package org.chorus.scoreboard

import cn.nukkit.Server
import cn.nukkit.event.scoreboard.ScoreboardLineChangeEvent
import cn.nukkit.scoreboard.scorer.IScorer
import lombok.Getter

@Getter
class ScoreboardLine @JvmOverloads constructor(
    override val scoreboard: IScoreboard,
    override val scorer: IScorer?,
    override var score: Int = 0
) :
    IScoreboardLine {
    override val lineId: Long

    init {
        this.lineId = ++staticLineId
    }

    override fun setScore(score: Int): Boolean {
        var score = score
        if (scoreboard.shouldCallEvent()) {
            val event = ScoreboardLineChangeEvent(
                scoreboard,
                this,
                score,
                this.score,
                ScoreboardLineChangeEvent.ActionType.SCORE_CHANGE
            )
            Server.getInstance().pluginManager.callEvent(event)
            if (event.isCancelled) {
                return false
            }
            score = event.getNewValue()
        }
        this.score = score
        updateScore()
        return true
    }

    companion object {
        protected var staticLineId: Long = 0
    }
}
