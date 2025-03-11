package org.chorus.scoreboard

import org.chorus.network.protocol.SetScorePacket.ScoreInfo
import org.chorus.scoreboard.scorer.IScorer

/**
 * 计分板上的单个行 <br></br>
 * 由[IScorer]和分数组成
 */
interface IScoreboardLine {
    /**
     * 获取追踪对象
     * @return 追踪对象
     */
    val scorer: IScorer

    /**
     * 获取行id
     * 客户端通过此id标识计分板上的每个行
     * @return 行id
     */
    val lineId: Long

    /**
     * 获取此计分行所属的计分板
     * @return 所属计分板
     */
    val scoreboard: IScoreboard

    /**
     * 获取分数
     * @return 分数
     */
    val score: Int

    /**
     * 设置分数
     * @param score 分数
     * @return 是否成功（事件被撤回就会false）
     */
    fun setScore(score: Int): Boolean

    /**
     * 增加分数
     * @param addition 增加量
     * @return 是否成功（事件被撤回就会false）
     */
    fun addScore(addition: Int): Boolean {
        return setScore(score + addition)
    }

    /**
     * 减少分数
     * @param reduction 减少量
     * @return 是否成功（事件被撤回就会false）
     */
    fun removeScore(reduction: Int): Boolean {
        return setScore(score - reduction)
    }

    /**
     * 内部方法
     * 转换到network信息
     * @return network信息
     */
    fun toNetworkInfo(): ScoreInfo? {
        return scorer.toNetworkInfo(scoreboard, this)
    }

    /**
     * 内部方法
     * 通知所属计分板对象更新此行信息
     */
    fun updateScore() {
        scoreboard.updateScore(this)
    }
}
