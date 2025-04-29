package org.chorus_oss.chorus.network.query.enveloped

import io.netty.channel.DefaultAddressedEnvelope
import org.chorus_oss.chorus.network.query.QueryPacket
import java.net.InetSocketAddress


class DirectAddressedQueryPacket : DefaultAddressedEnvelope<QueryPacket?, InetSocketAddress?> {
    constructor(message: QueryPacket, recipient: InetSocketAddress?, sender: InetSocketAddress?) : super(
        message,
        recipient,
        sender
    )

    constructor(message: QueryPacket, recipient: InetSocketAddress?) : super(message, recipient)
}
