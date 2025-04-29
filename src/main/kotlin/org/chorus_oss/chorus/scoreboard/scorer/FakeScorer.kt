package org.chorus_oss.chorus.scoreboard.scorer

import org.chorus_oss.chorus.network.protocol.SetScorePacket.ScoreInfo
import org.chorus_oss.chorus.scoreboard.IScoreboard
import org.chorus_oss.chorus.scoreboard.IScoreboardLine
import org.chorus_oss.chorus.scoreboard.data.ScorerType

class FakeScorer(override val name: String) : IScorer {
    override val scorerType: ScorerType
        get() = ScorerType.FAKE

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is FakeScorer) {
            return other.name == name
        }
        return false
    }

    override fun toNetworkInfo(scoreboard: IScoreboard, line: IScoreboardLine): ScoreInfo {
        return ScoreInfo(line.lineId, scoreboard.objectiveName, line.score, name)
    }
}
