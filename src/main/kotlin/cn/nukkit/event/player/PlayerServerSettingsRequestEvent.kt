package cn.nukkit.event.player

import cn.nukkit.Player
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.form.window.Form

/**
 * @author CreeperFace
 */
class PlayerServerSettingsRequestEvent(player: Player?, settings: MutableMap<Int, Form<*>>) :
    PlayerEvent(), Cancellable {
    private var settings: MutableMap<Int, Form<*>>

    init {
        this.player = player
        this.settings = settings
    }

    fun getSettings(): Map<Int, Form<*>> {
        return settings
    }

    fun setSettings(settings: MutableMap<Int, Form<*>>) {
        this.settings = settings
    }

    fun setSettings(id: Int, window: Form<*>) {
        settings[id] = window
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
