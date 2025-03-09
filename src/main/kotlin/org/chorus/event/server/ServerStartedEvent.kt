package org.chorus.event.server

import org.chorus.event.HandlerList

/**
 * 服务器启动完毕后会触发，注意reload也会触发
 */
object ServerStartedEvent : ServerEvent() {
    val handlers: HandlerList = HandlerList()
}
