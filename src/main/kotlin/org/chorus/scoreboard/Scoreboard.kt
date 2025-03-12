package org.chorus.scoreboard

import org.chorus.Server
import org.chorus.event.scoreboard.ScoreboardLineChangeEvent
import org.chorus.scoreboard.data.DisplaySlot
import org.chorus.scoreboard.data.SortOrder
import org.chorus.scoreboard.displayer.IScoreboardViewer
import org.chorus.scoreboard.scorer.FakeScorer
import org.chorus.scoreboard.scorer.IScorer


import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer


class Scoreboard @JvmOverloads constructor(
    override var objectiveName: String,
    override var displayName: String,
    override var criteriaName: String = "dummy",
    override var sortOrder: SortOrder = SortOrder.ASCENDING
) :
    IScoreboard {
    protected var viewers: MutableMap<DisplaySlot, MutableSet<IScoreboardViewer>> = HashMap()
    override var lines: MutableMap<IScorer?, IScoreboardLine> = HashMap()

    init {
        for (slot in DisplaySlot.entries) {
            viewers[slot] = HashSet()
        }
    }

    override val allViewers: Set<IScoreboardViewer>
        get() {
            val all = HashSet<IScoreboardViewer>()
            viewers.values.forEach(Consumer<Set<IScoreboardViewer>> { c: Set<IScoreboardViewer>? ->
                all.addAll(
                    c!!
                )
            })
            return all
        }

    override fun getViewers(slot: DisplaySlot): Set<IScoreboardViewer> {
        return viewers[slot]!!
    }

    override fun removeViewer(viewer: IScoreboardViewer, slot: DisplaySlot): Boolean {
        val removed = viewers[slot]!!.remove(viewer)
        if (removed) viewer.hide(slot)
        return removed
    }

    override fun addViewer(viewer: IScoreboardViewer, slot: DisplaySlot): Boolean {
        val added = viewers[slot]!!.add(viewer)
        if (added) viewer.display(this, slot)
        return added
    }

    override fun containViewer(viewer: IScoreboardViewer, slot: DisplaySlot): Boolean {
        return viewers[slot]!!.contains(viewer)
    }

    override fun getLine(scorer: IScorer?): IScoreboardLine? {
        return lines[scorer]
    }

    override fun addLine(line: IScoreboardLine): Boolean {
        var line = line
        if (shouldCallEvent()) {
            val event = ScoreboardLineChangeEvent(
                this,
                line,
                line.score,
                line.score,
                ScoreboardLineChangeEvent.ActionType.ADD_LINE
            )
            Server.instance.pluginManager.callEvent(event)
            if (event.isCancelled) return false
            line = event.getLine()
        }
        lines[line.scorer] = line
        updateScore(line)
        return true
    }

    override fun addLine(scorer: IScorer?, score: Int): Boolean {
        return addLine(ScoreboardLine(this, scorer, score))
    }

    override fun addLine(text: String, score: Int): Boolean {
        val fakeScorer = FakeScorer(text)
        return addLine(ScoreboardLine(this, fakeScorer, score))
    }

    override fun removeLine(scorer: IScorer?): Boolean {
        val removed = lines[scorer] ?: return false
        if (shouldCallEvent()) {
            val event = ScoreboardLineChangeEvent(
                this,
                removed,
                removed.score,
                removed.score,
                ScoreboardLineChangeEvent.ActionType.REMOVE_LINE
            )
            Server.instance.pluginManager.callEvent(event)
            if (event.isCancelled) return false
        }
        lines.remove(scorer)
        allViewers.forEach(Consumer { viewer: IScoreboardViewer -> viewer.removeLine(removed) })
        return true
    }

    override fun removeAllLine(send: Boolean): Boolean {
        if (lines.isEmpty()) return false
        if (shouldCallEvent()) {
            val event =
                ScoreboardLineChangeEvent(this, null, 0, 0, ScoreboardLineChangeEvent.ActionType.REMOVE_ALL_LINES)
            Server.instance.pluginManager.callEvent(event)
            if (event.isCancelled) return false
        }
        if (send) {
            lines.keys.forEach(Consumer { scorer: IScorer? -> this.removeLine(scorer) })
        } else {
            lines.clear()
        }
        return true
    }

    override fun containLine(scorer: IScorer?): Boolean {
        return lines.containsKey(scorer)
    }

    override fun updateScore(update: IScoreboardLine?) {
        allViewers.forEach(Consumer { viewer: IScoreboardViewer -> viewer.updateScore(update) })
    }

    override fun resend() {
        allViewers.forEach(Consumer { viewer: IScoreboardViewer -> viewer.removeScoreboard(this) })

        viewers.forEach { (slot: DisplaySlot?, slotViewers: MutableSet<IScoreboardViewer?>?) ->
            slotViewers.forEach(
                Consumer { slotViewer: IScoreboardViewer ->
                    slotViewer.display(this, slot)
                })
        }
    }

    override fun setLines(lines: List<String>) {
        removeAllLine(false)
        val score = AtomicInteger()
        lines.forEach(Consumer { str: String ->
            val scorer = FakeScorer(str)
            this.lines[scorer] = ScoreboardLine(this, scorer, score.getAndIncrement())
        })
        resend()
    }

    override fun setLines(lines: Collection<IScoreboardLine>) {
        removeAllLine(false)
        lines.forEach(Consumer { line: IScoreboardLine ->
            this.lines[line.scorer] =
                line
        })
        resend()
    }

    override fun shouldCallEvent(): Boolean {
        val manager = Server.instance.scoreboardManager
        return manager != null && manager.containScoreboard(this)
    }
}
