package org.chorus.event.scoreboard

import org.chorus.event.HandlerList
import org.chorus.scoreboard.IScoreboard

class ScoreboardObjectiveChangeEvent(scoreboard: IScoreboard, val actionType: ActionType) :
    ScoreboardEvent(scoreboard) {
    enum class ActionType {
        ADD,
        REMOVE
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
