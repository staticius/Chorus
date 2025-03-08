package cn.nukkit.network.query.enveloped

import cn.nukkit.network.query.QueryPacket
import io.netty.channel.DefaultAddressedEnvelope
import java.net.InetSocketAddress


class DirectAddressedQueryPacket : DefaultAddressedEnvelope<QueryPacket?, InetSocketAddress?> {
    constructor(message: QueryPacket, recipient: InetSocketAddress?, sender: InetSocketAddress?) : super(
        message,
        recipient,
        sender
    )

    constructor(message: QueryPacket, recipient: InetSocketAddress?) : super(message, recipient)
}
