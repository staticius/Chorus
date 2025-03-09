package org.chorus.network.query

import org.chorus.event.server.QueryRegenerateEvent
import java.net.InetSocketAddress

fun interface QueryEventListener {
    fun onQuery(address: InetSocketAddress?): QueryRegenerateEvent?
}
