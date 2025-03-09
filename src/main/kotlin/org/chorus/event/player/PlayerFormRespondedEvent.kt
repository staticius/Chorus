package org.chorus.event.player

import cn.nukkit.Player
import cn.nukkit.event.HandlerList
import cn.nukkit.form.response.Response
import cn.nukkit.form.window.Form

class PlayerFormRespondedEvent(player: Player?, formID: Int, window: Form<*>, response: Response) :
    PlayerEvent() {
    var formID: Int
        protected set
    var window: Form<*>
        protected set

    /**
     * Can be null if player closed the window instead of submitting it
     *
     * @return response
     */
    var response: Response
        protected set

    init {
        this.player = player
        this.formID = formID
        this.window = window
        this.response = response
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
