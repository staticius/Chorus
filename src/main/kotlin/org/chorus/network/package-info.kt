/**
 * 与基岩版网络协议相关的类.
 *
 *
 * Classes relevant to bedrock edition network protocols.
 */
package org.chorus.network

import org.chorus.lang.BaseLang.get
import org.chorus.network.connection.BedrockPong.toByteBuf
import org.chorus.config.ServerProperties.get
import org.chorus.network.connection.netty.initializer.BedrockChannelInitializer.createPeer
import org.chorus.network.connection.BedrockSession.socketAddress
import org.chorus.network.connection.BedrockSession.close
import org.chorus.lang.BaseLang.tr
import org.chorus.network.connection.BedrockSession.ping
import org.chorus.network.connection.BedrockSession.isConnected
import org.chorus.network.connection.BedrockSession.player
import java.util.concurrent.ThreadFactory
import java.util.LinkedList
import org.chorus.network.Network.NetWorkStatisticData
import java.util.concurrent.atomic.AtomicReference
import oshi.hardware.NetworkIF
import java.net.InetSocketAddress
import org.chorus.network.connection.BedrockSession
import java.util.concurrent.ConcurrentHashMap
import java.net.InetAddress
import java.util.HashMap
import org.cloudburstmc.netty.channel.raknet.RakServerChannel
import org.chorus.network.connection.BedrockPong
import org.chorus.plugin.InternalPlugin
import io.netty.channel.EventLoopGroup
import io.netty.channel.epoll.Epoll
import io.netty.channel.epoll.EpollDatagramChannel
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.kqueue.KQueue
import io.netty.channel.kqueue.KQueueDatagramChannel
import io.netty.channel.kqueue.KQueueEventLoopGroup
import io.netty.channel.socket.nio.NioDatagramChannel
import io.netty.channel.nio.NioEventLoopGroup
import org.chorus.network.protocol.ProtocolInfo
import org.cloudburstmc.netty.channel.raknet.RakChannelFactory
import io.netty.buffer.ByteBuf
import org.cloudburstmc.netty.channel.raknet.config.RakChannelOption
import org.chorus.network.connection.netty.initializer.BedrockServerInitializer
import org.chorus.config.ServerPropertiesKeys
import org.chorus.network.query.codec.QueryPacketCodec
import org.chorus.network.query.handler.QueryPacketHandler
import org.chorus.network.query.QueryEventListener
import org.chorus.network.connection.BedrockPeer
import java.time.temporal.ChronoUnit

