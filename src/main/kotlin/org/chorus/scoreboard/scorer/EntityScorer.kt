package org.chorus.scoreboard.scorer

import org.chorus.entity.Entity
import org.chorus.network.protocol.SetScorePacket.ScoreInfo
import org.chorus.scoreboard.IScoreboard
import org.chorus.scoreboard.IScoreboardLine
import org.chorus.scoreboard.data.ScorerType

import java.util.*


class EntityScorer : IScorer {
    val uniqueID: Long

    constructor(uuid: UUID) {
        this.uniqueID = uuid.mostSignificantBits
    }

    constructor(entity: Entity) {
        this.uniqueID = entity.uniqueId
    }

    override val scorerType: ScorerType
        get() = ScorerType.ENTITY

    override fun hashCode(): Int {
        return uniqueID.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is EntityScorer) {
            return uniqueID == other.uniqueID
        }
        return false
    }

    override val name: String
        get() = uniqueID.toString()

    override fun toNetworkInfo(scoreboard: IScoreboard, line: IScoreboardLine): ScoreInfo {
        return ScoreInfo(
            line.lineId,
            scoreboard.objectiveName,
            line.score,
            ScorerType.ENTITY,
            uniqueID
        )
    }
}
