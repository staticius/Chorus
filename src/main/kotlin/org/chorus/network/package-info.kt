/**
 * 与基岩版网络协议相关的类.
 *
 *
 * Classes relevant to bedrock edition network protocols.
 */
package org.chorus.network

import cn.nukkit.lang.BaseLang.get
import cn.nukkit.network.connection.BedrockPong.toByteBuf
import cn.nukkit.config.ServerProperties.get
import cn.nukkit.network.connection.netty.initializer.BedrockChannelInitializer.createPeer
import cn.nukkit.network.connection.BedrockSession.socketAddress
import cn.nukkit.network.connection.BedrockSession.close
import cn.nukkit.lang.BaseLang.tr
import cn.nukkit.network.connection.BedrockSession.ping
import cn.nukkit.network.connection.BedrockSession.isConnected
import cn.nukkit.network.connection.BedrockSession.player
import java.util.concurrent.ThreadFactory
import java.util.LinkedList
import cn.nukkit.network.Network.NetWorkStatisticData
import java.util.concurrent.atomic.AtomicReference
import oshi.hardware.NetworkIF
import java.net.InetSocketAddress
import cn.nukkit.network.connection.BedrockSession
import java.util.concurrent.ConcurrentHashMap
import java.net.InetAddress
import java.util.HashMap
import org.cloudburstmc.netty.channel.raknet.RakServerChannel
import cn.nukkit.network.connection.BedrockPong
import cn.nukkit.plugin.InternalPlugin
import io.netty.channel.EventLoopGroup
import io.netty.channel.epoll.Epoll
import io.netty.channel.epoll.EpollDatagramChannel
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.kqueue.KQueue
import io.netty.channel.kqueue.KQueueDatagramChannel
import io.netty.channel.kqueue.KQueueEventLoopGroup
import io.netty.channel.socket.nio.NioDatagramChannel
import io.netty.channel.nio.NioEventLoopGroup
import cn.nukkit.network.protocol.ProtocolInfo
import org.cloudburstmc.netty.channel.raknet.RakChannelFactory
import io.netty.buffer.ByteBuf
import org.cloudburstmc.netty.channel.raknet.config.RakChannelOption
import cn.nukkit.network.connection.netty.initializer.BedrockServerInitializer
import cn.nukkit.config.ServerPropertiesKeys
import cn.nukkit.network.query.codec.QueryPacketCodec
import cn.nukkit.network.query.handler.QueryPacketHandler
import cn.nukkit.network.query.QueryEventListener
import cn.nukkit.network.connection.BedrockPeer
import java.time.temporal.ChronoUnit

