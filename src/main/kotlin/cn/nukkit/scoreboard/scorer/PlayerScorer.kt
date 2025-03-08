package cn.nukkit.scoreboard.scorer

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.entity.EntityHuman.getName
import cn.nukkit.network.protocol.SetScorePacket.ScoreInfo
import cn.nukkit.scoreboard.IScoreboard
import cn.nukkit.scoreboard.IScoreboardLine
import cn.nukkit.scoreboard.data.ScorerType
import lombok.Getter
import java.util.*

@Getter
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
