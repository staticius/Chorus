package org.chorus.scoreboard

import org.chorus.Server
import org.chorus.event.scoreboard.ScoreboardLineChangeEvent
import org.chorus.scoreboard.scorer.IScorer



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
