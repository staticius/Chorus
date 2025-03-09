package org.chorus.event.player

/**
 * @author xtypr
 * @since 2015/12/23
 */
abstract class PlayerMessageEvent : PlayerEvent() {
    @JvmField
    var message: String? = null
}
