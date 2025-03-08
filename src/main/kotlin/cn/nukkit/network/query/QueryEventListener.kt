package cn.nukkit.network.query

import cn.nukkit.event.server.QueryRegenerateEvent
import java.net.InetSocketAddress

fun interface QueryEventListener {
    fun onQuery(address: InetSocketAddress?): QueryRegenerateEvent?
}
