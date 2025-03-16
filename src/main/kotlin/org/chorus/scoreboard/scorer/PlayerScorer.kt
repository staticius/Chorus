package org.chorus.scoreboard.scorer

import org.chorus.Player
import org.chorus.Server
import org.chorus.network.protocol.SetScorePacket.ScoreInfo
import org.chorus.scoreboard.IScoreboard
import org.chorus.scoreboard.IScoreboardLine
import org.chorus.scoreboard.data.ScorerType

import java.util.*


class PlayerScorer : IScorer {
    val uuid: UUID

    constructor(uuid: UUID) {
        this.uuid = uuid
    }

    constructor(uuid: String) {
        this.uuid = UUID.fromString(uuid)
    }

    constructor(player: Player) {
        this.uuid = player.uuid
    }

    val player: Player?
        get() {
            return if (Server.instance.getPlayer(uuid).isPresent) Server.instance.getPlayer(uuid).get() else null
        }

    val isOnline: Boolean
        get() = player != null

    override val scorerType: ScorerType
        get() = ScorerType.PLAYER

    override fun hashCode(): Int {
        return uuid.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is PlayerScorer) {
            return uuid == other.uuid
        }
        return false
    }

    override val name: String
        get() = if (Server.instance.onlinePlayers[uuid] == null) uuid.mostSignificantBits
            .toString() else Server.instance.onlinePlayers[uuid]!!.getName()

    override fun toNetworkInfo(scoreboard: IScoreboard, line: IScoreboardLine): ScoreInfo? {
        return if (Server.instance.getPlayer(uuid).isPresent) ScoreInfo(
            line.lineId,
            scoreboard.objectiveName,
            line.score,
            ScorerType.PLAYER,
            Server.instance.getPlayer(uuid).get().getId()
        ) else null
    }
}
