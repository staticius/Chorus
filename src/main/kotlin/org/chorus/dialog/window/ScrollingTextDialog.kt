package org.chorus.dialog.window

import org.chorus.Player
import org.chorus.Server
import org.chorus.scheduler.Task

class ScrollingTextDialog @JvmOverloads constructor(
    var player: Player,
    var dialog: FormWindowDialog,
    scrollingSpeed: Int = 2
) :
    Dialog {
    var scrollingSpeed: Int //unit: gt
    var isScrolling: Boolean = false
    private var cursor = 0
    private val scrollingTask: Task

    init {
        scrollingTask = ScrollingRunner()
        this.scrollingSpeed = scrollingSpeed
    }

    fun stopScrolling() {
        isScrolling = false
    }

    fun getCursor(): Int {
        return cursor
    }

    fun setCursor(cursor: Int) {
        require(cursor <= dialog.content.length) { "cursor cannot bigger than the origin string's length" }
        this.cursor = cursor
    }

    fun startScrolling() {
        this.isScrolling = true
        Server.instance.scheduler.scheduleRepeatingTask(this.scrollingTask, this.scrollingSpeed)
    }

    override fun send(p: Player) {
        this.startScrolling()
    }

    private inner class ScrollingRunner : Task() {
        private val clone = FormWindowDialog(dialog.title, dialog.content, dialog.bindEntity)

        override fun onRun(currentTick: Int) {
            if (!this.isScrolling || cursor >= dialog.content.length) {
                cursor = 0
                player.showDialogWindow(dialog)
                stopScrolling()
                this.cancel()
                return
            }
            clone.content = dialog.content.substring(0, cursor)
            player.showDialogWindow(clone)
            if (dialog.content.length - (cursor + 1) >= 2 && dialog.content[cursor] == '§') cursor += 2
            else cursor++
        }
    }
}
