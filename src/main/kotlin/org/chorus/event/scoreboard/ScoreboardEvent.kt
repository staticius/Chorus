package org.chorus.event.scoreboard

import org.chorus.event.Cancellable
import org.chorus.event.Event
import org.chorus.event.HandlerList
import org.chorus.scoreboard.IScoreboard


/**
 * 请注意，若计分板不存在于 [org.chorus.Server]::scoreboardManager中，则此事件不会被调用
 */

abstract class ScoreboardEvent(val scoreboard: IScoreboard) : Event(), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
