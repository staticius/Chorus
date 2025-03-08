package cn.nukkit.scoreboard.scorer

import cn.nukkit.entity.Entity
import cn.nukkit.network.protocol.SetScorePacket.ScoreInfo
import cn.nukkit.scoreboard.IScoreboard
import cn.nukkit.scoreboard.IScoreboardLine
import cn.nukkit.scoreboard.data.ScorerType
import lombok.Getter
import java.util.*

@Getter
class EntityScorer : IScorer {
    private val entityUuid: UUID?

    constructor(uuid: UUID?) {
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

    override fun equals(obj: Any?): Boolean {
        if (obj is EntityScorer) {
            return entityUuid == obj.entityUuid
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
            entityUuid!!.mostSignificantBits
        )
    }
}
