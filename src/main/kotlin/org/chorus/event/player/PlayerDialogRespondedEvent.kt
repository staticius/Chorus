package org.chorus.event.player

import org.chorus.Player
import org.chorus.dialog.response.FormResponseDialog
import org.chorus.dialog.window.FormWindowDialog
import org.chorus.event.HandlerList

class PlayerDialogRespondedEvent(player: Player?, var dialog: FormWindowDialog, var response: FormResponseDialog) :
    PlayerEvent() {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
