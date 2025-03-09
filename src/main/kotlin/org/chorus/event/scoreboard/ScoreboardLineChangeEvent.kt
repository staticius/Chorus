package org.chorus.event.scoreboard

import cn.nukkit.event.HandlerList
import cn.nukkit.scoreboard.IScoreboard
import cn.nukkit.scoreboard.IScoreboardLine
import lombok.Getter
import lombok.Setter

@Getter
@Setter
class ScoreboardLineChangeEvent @JvmOverloads constructor(
    scoreboard: IScoreboard,
    private val line: IScoreboardLine?,
    private val newValue: Int,
    private val oldValue: Int,
    private val actionType: ActionType = ActionType.SCORE_CHANGE
) :
    ScoreboardEvent(scoreboard) {
    enum class ActionType {
        SCORE_CHANGE,
        REMOVE_LINE,
        REMOVE_ALL_LINES,
        ADD_LINE
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
