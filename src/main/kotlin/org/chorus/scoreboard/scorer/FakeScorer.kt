package org.chorus.scoreboard.scorer

import cn.nukkit.network.protocol.SetScorePacket.ScoreInfo
import cn.nukkit.scoreboard.IScoreboard
import cn.nukkit.scoreboard.IScoreboardLine
import cn.nukkit.scoreboard.data.ScorerType
import lombok.Getter

@Getter
class FakeScorer(override val name: String) : IScorer {
    override val scorerType: ScorerType
        get() = ScorerType.FAKE

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun equals(obj: Any?): Boolean {
        if (obj is FakeScorer) {
            return obj.name == name
        }
        return false
    }

    override fun toNetworkInfo(scoreboard: IScoreboard, line: IScoreboardLine): ScoreInfo {
        return ScoreInfo(line.lineId, scoreboard.objectiveName, line.score, getFakeName())
    }
}
