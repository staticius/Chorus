package cn.nukkit.scoreboard.manager

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.command.data.CommandEnum
import cn.nukkit.entity.EntityLiving
import cn.nukkit.event.scoreboard.ScoreboardObjectiveChangeEvent
import cn.nukkit.network.protocol.UpdateSoftEnumPacket
import cn.nukkit.scoreboard.IScoreboard
import cn.nukkit.scoreboard.data.DisplaySlot
import cn.nukkit.scoreboard.displayer.IScoreboardViewer
import cn.nukkit.scoreboard.scorer.EntityScorer
import cn.nukkit.scoreboard.scorer.PlayerScorer
import cn.nukkit.scoreboard.storage.IScoreboardStorage
import lombok.Getter
import java.util.function.Consumer


@Getter
class ScoreboardManager(override var storage: IScoreboardStorage) : IScoreboardManager {
    override var scoreboards: MutableMap<String?, IScoreboard?>? = HashMap()
    override var display: MutableMap<DisplaySlot, IScoreboard?> = HashMap()
    override var viewers: MutableSet<IScoreboardViewer> = HashSet()

    init {
        read()
    }

    override fun addScoreboard(scoreboard: IScoreboard): Boolean {
        val event = ScoreboardObjectiveChangeEvent(scoreboard, ScoreboardObjectiveChangeEvent.ActionType.ADD)
        Server.getInstance().pluginManager.callEvent(event)
        if (event.isCancelled) {
            return false
        }
        scoreboards!![scoreboard.objectiveName] = scoreboard
        CommandEnum.SCOREBOARD_OBJECTIVES.updateSoftEnum(UpdateSoftEnumPacket.Type.ADD, scoreboard.objectiveName)
        return true
    }

    override fun removeScoreboard(scoreboard: IScoreboard): Boolean {
        return removeScoreboard(scoreboard.objectiveName)
    }

    override fun removeScoreboard(objectiveName: String): Boolean {
        val removed = scoreboards!![objectiveName] ?: return false
        val event = ScoreboardObjectiveChangeEvent(removed, ScoreboardObjectiveChangeEvent.ActionType.REMOVE)
        Server.getInstance().pluginManager.callEvent(event)
        if (event.isCancelled) {
            return false
        }
        scoreboards!!.remove(objectiveName)
        CommandEnum.SCOREBOARD_OBJECTIVES.updateSoftEnum(UpdateSoftEnumPacket.Type.REMOVE, objectiveName)
        viewers.forEach(Consumer { viewer: IScoreboardViewer -> viewer.removeScoreboard(removed) })
        display.forEach { (slot: DisplaySlot?, scoreboard: IScoreboard?) ->
            if (scoreboard != null && scoreboard.objectiveName == objectiveName) {
                display[slot] = null
            }
        }
        return true
    }

    override fun getScoreboard(objectiveName: String?): IScoreboard? {
        return scoreboards!![objectiveName]
    }

    override fun containScoreboard(scoreboard: IScoreboard): Boolean {
        return scoreboards!!.containsKey(scoreboard.objectiveName)
    }

    override fun containScoreboard(name: String?): Boolean {
        return scoreboards!!.containsKey(name)
    }

    override fun getDisplaySlot(slot: DisplaySlot): IScoreboard? {
        return display[slot]
    }

    override fun setDisplay(slot: DisplaySlot, scoreboard: IScoreboard?) {
        val old = display.put(slot, scoreboard)
        if (old != null) viewers.forEach(Consumer { viewer: IScoreboardViewer -> old.removeViewer(viewer, slot) })
        if (scoreboard != null) viewers.forEach(Consumer { viewer: IScoreboardViewer ->
            scoreboard.addViewer(
                viewer,
                slot
            )
        })
    }

    override fun addViewer(viewer: IScoreboardViewer): Boolean {
        val added = viewers.add(viewer)
        if (added) display.forEach { (slot: DisplaySlot?, scoreboard: IScoreboard?) ->
            scoreboard?.addViewer(viewer, slot)
        }
        return added
    }

    override fun removeViewer(viewer: IScoreboardViewer): Boolean {
        val removed = viewers.remove(viewer)
        if (removed) display.forEach { (slot: DisplaySlot?, scoreboard: IScoreboard?) ->
            scoreboard?.removeViewer(viewer, slot)
        }
        return removed
    }

    /**
     * 你可能会对这里的代码感到疑惑
     * 这里其实是为了规避玩家下线导致的“玩家离线”计分项
     * 我们在玩家下线时，删除其他在线玩家显示槽位中的“玩家离线”
     * 并在其重新连接上服务器时加回去
     */
    override fun onPlayerJoin(player: Player) {
        addViewer(player)
        val scorer = PlayerScorer(player)
        scoreboards!!.values.forEach(Consumer { scoreboard: IScoreboard? ->
            if (scoreboard!!.containLine(scorer)) {
                viewers.forEach(Consumer { viewer: IScoreboardViewer ->
                    viewer.updateScore(
                        scoreboard.getLine(scorer)
                    )
                })
            }
        })
    }

    override fun beforePlayerQuit(player: Player) {
        val scorer = PlayerScorer(player)
        scoreboards!!.values.forEach(Consumer { scoreboard: IScoreboard? ->
            if (scoreboard!!.containLine(scorer)) {
                viewers.forEach(Consumer { viewer: IScoreboardViewer ->
                    viewer.removeLine(
                        scoreboard.getLine(scorer)
                    )
                })
            }
        })
        removeViewer(player)
    }

    override fun onEntityDead(entity: EntityLiving) {
        val scorer = EntityScorer(entity)
        scoreboards!!.forEach { (s: String?, scoreboard: IScoreboard?) ->
            if (scoreboard.getLines().isEmpty()) return@forEach
            scoreboard!!.removeLine(scorer)
        }
    }

    override fun save() {
        storage.removeAllScoreboard()
        storage.saveScoreboard(scoreboards!!.values)
        storage.saveDisplay(display)
    }

    override fun read() {
        //新建一个列表避免迭代冲突
        ArrayList(scoreboards!!.values).forEach(Consumer { scoreboard: IScoreboard? ->
            this.removeScoreboard(
                scoreboard!!
            )
        })
        display.forEach { (slot: DisplaySlot?, scoreboard: IScoreboard?) -> setDisplay(slot, null) }

        scoreboards = storage.readScoreboard()
        storage.readDisplay().forEach { (slot: DisplaySlot?, objectiveName: String?) ->
            val scoreboard = getScoreboard(objectiveName)
            if (scoreboard != null) {
                this.setDisplay(slot, scoreboard)
            }
        }
    }
}
