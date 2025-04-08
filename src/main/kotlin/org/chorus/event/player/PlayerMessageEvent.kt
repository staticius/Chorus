package org.chorus.event.player

abstract class PlayerMessageEvent : PlayerEvent() {
    @JvmField
    var message: String? = null
}
