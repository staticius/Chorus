package cn.nukkit.event.player

import cn.nukkit.Player
import cn.nukkit.dialog.response.FormResponseDialog
import cn.nukkit.dialog.window.FormWindowDialog
import cn.nukkit.event.HandlerList

class PlayerDialogRespondedEvent(player: Player?, var dialog: FormWindowDialog, var response: FormResponseDialog) :
    PlayerEvent() {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
