package org.chorus.scoreboard.scorer

import org.chorus.entity.Entity
import org.chorus.network.protocol.SetScorePacket.ScoreInfo
import org.chorus.scoreboard.IScoreboard
import org.chorus.scoreboard.IScoreboardLine
import org.chorus.scoreboard.data.ScorerType

import java.util.*


class EntityScorer : IScorer {
    val entityUuid: UUID

    constructor(uuid: UUID) {
        this.entityUuid = uuid
    }

    constructor(entity: Entity) {
        this.entityUuid = entity.getUniqueId()
    }

    override val scorerType: ScorerType
        get() = ScorerType.ENTITY

    override fun hashCode(): Int {
        return entityUuid.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is EntityScorer) {
            return entityUuid == other.entityUuid
        }
        return false
    }

    override val name: String
        get() = entityUuid.toString()

    override fun toNetworkInfo(scoreboard: IScoreboard, line: IScoreboardLine): ScoreInfo {
        return ScoreInfo(
            line.lineId,
            scoreboard.objectiveName,
            line.score,
            ScorerType.ENTITY,
            entityUuid.mostSignificantBits
        )
    }
}
