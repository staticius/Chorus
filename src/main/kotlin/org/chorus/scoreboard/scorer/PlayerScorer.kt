package org.chorus.scoreboard.scorer

import org.chorus.Player
import org.chorus.Server
import org.chorus.entity.EntityHuman.getName
import org.chorus.network.protocol.SetScorePacket.ScoreInfo
import org.chorus.scoreboard.IScoreboard
import org.chorus.scoreboard.IScoreboardLine
import org.chorus.scoreboard.data.ScorerType
import lombok.Getter
import java.util.*


class PlayerScorer : IScorer {
    private val uuid: UUID?

    constructor(uuid: UUID?) {
        this.uuid = uuid
    }

    constructor(uuid: String) {
        this.uuid = UUID.fromString(uuid)
    }

    constructor(player: Player) {
        this.uuid = player.uniqueId
    }

    val player: Player?
        get() {
            if (uuid == null) return null
            return if (Server.getInstance().getPlayer(uuid).isPresent) Server.getInstance()
                .getPlayer(uuid).get() else null
        }

    val isOnline: Boolean
        get() = player != null

    override val scorerType: ScorerType
        get() = ScorerType.PLAYER

    override fun hashCode(): Int {
        return uuid?.hashCode() ?: 0
    }

    override fun equals(obj: Any?): Boolean {
        if (obj is PlayerScorer) {
            return uuid == obj.uuid
        }
        return false
    }

    override val name: String
        get() = if (Server.getInstance().onlinePlayers[uuid] == null) uuid!!.mostSignificantBits
            .toString() else Server.getInstance().onlinePlayers[uuid].getName()

    override fun toNetworkInfo(scoreboard: IScoreboard, line: IScoreboardLine): ScoreInfo? {
        if (uuid == null) return null
        return if (Server.getInstance().getPlayer(uuid).isPresent) ScoreInfo(
            line.lineId,
            scoreboard.objectiveName,
            line.score,
            ScorerType.PLAYER,
            Server.getInstance().getPlayer(uuid).get().getId()
        ) else null
    }
}
