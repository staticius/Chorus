package org.chorus_oss.chorus.network.query

import org.chorus_oss.chorus.event.server.QueryRegenerateEvent
import java.net.InetSocketAddress

fun interface QueryEventListener {
    fun onQuery(address: InetSocketAddress?): QueryRegenerateEvent?
}
