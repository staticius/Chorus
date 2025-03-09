package org.chorus.event.scoreboard

import cn.nukkit.event.HandlerList
import cn.nukkit.scoreboard.IScoreboard

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
