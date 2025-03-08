package cn.nukkit.scoreboard.scorer

import cn.nukkit.network.protocol.SetScorePacket.ScoreInfo
import cn.nukkit.scoreboard.IScoreboard
import cn.nukkit.scoreboard.IScoreboardLine
import cn.nukkit.scoreboard.data.ScorerType

/**
 * 计分板追踪对象
 */
interface IScorer {
    /**
     * 获取追踪对象类型
     * @return 追踪对象类型
     */
    val scorerType: ScorerType

    /**
     * 获取名称
     * @return 追踪对象类型
     */
    val name: String

    /**
     * 内部方法
     * 转换到network信息
     * @param scoreboard 所属计分板
     * @param line 所属行
     * @return network信息
     */
    fun toNetworkInfo(scoreboard: IScoreboard, line: IScoreboardLine): ScoreInfo
}
