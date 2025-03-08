package cn.nukkit.event.scoreboard

import cn.nukkit.event.Cancellable
import cn.nukkit.event.Event
import cn.nukkit.event.HandlerList
import cn.nukkit.scoreboard.IScoreboard
import lombok.Getter

/**
 * 请注意，若计分板不存在于 [cn.nukkit.Server]::scoreboardManager中，则此事件不会被调用
 */
@Getter
abstract class ScoreboardEvent(val scoreboard: IScoreboard) : Event(), Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
