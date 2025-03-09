package org.chorus.network.rcon

import lombok.extern.slf4j.Slf4j
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.BufferUnderflowException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.nio.channels.spi.SelectorProvider
import java.nio.charset.Charset
import kotlin.concurrent.Volatile

/**
 * Thread that performs all RCON network work. A server.
 *
 * @author Tee7even
 */
@Slf4j
class RCONServer(address: String, port: Int, password: String) : Thread() {
    @Volatile
    private var running: Boolean

    private val serverChannel: ServerSocketChannel
    private val selector: Selector

    private val password: String
    private val rconSessions: MutableSet<SocketChannel> = HashSet()

    private val receiveQueue: MutableList<RCONCommand> = ArrayList()
    private val sendQueues: MutableMap<SocketChannel, MutableList<RCONPacket>> = HashMap()

    init {
        this.name = "RCON"
        this.running = true

        this.serverChannel = ServerSocketChannel.open()
        serverChannel.configureBlocking(false)
        serverChannel.socket().bind(InetSocketAddress(address, port))

        this.selector = SelectorProvider.provider().openSelector()
        serverChannel.register(this.selector, SelectionKey.OP_ACCEPT)

        this.password = password
    }

    fun receive(): RCONCommand? {
        synchronized(this.receiveQueue) {
            if (!receiveQueue.isEmpty()) {
                val command = receiveQueue[0]
                receiveQueue.removeAt(0)
                return command
            }
            return null
        }
    }

    fun respond(channel: SocketChannel, id: Int, response: String) {
        this.send(channel, RCONPacket(id, SERVERDATA_RESPONSE_VALUE, response.toByteArray()))
    }

    fun close() {
        this.running = false
        selector.wakeup()
    }

    override fun run() {
        while (this.running) {
            try {
                synchronized(this.sendQueues) {
                    for (channel in sendQueues.keys) {
                        channel.keyFor(this.selector).interestOps(SelectionKey.OP_WRITE)
                    }
                }

                selector.select()

                val selectedKeys = selector.selectedKeys().iterator()
                while (selectedKeys.hasNext()) {
                    val key = selectedKeys.next()
                    selectedKeys.remove()

                    if (key.isAcceptable) {
                        val serverSocketChannel = key.channel() as ServerSocketChannel

                        val socketChannel = serverSocketChannel.accept()
                        socketChannel.socket()
                        socketChannel.configureBlocking(false)
                        socketChannel.register(this.selector, SelectionKey.OP_READ)
                    } else if (key.isReadable) {
                        this.read(key)
                    } else if (key.isWritable) {
                        this.write(key)
                    }
                }
            } catch (exception: BufferUnderflowException) {
                RCONServer.log.trace("Got a possible corrupt packet", exception)
                //Corrupted packet, ignore
            } catch (exception: Exception) {
                RCONServer.log.error("An exception happened processing the RCON server", exception)
            }
        }

        try {
            serverChannel.keyFor(this.selector).cancel()
            serverChannel.close()
            selector.close()
        } catch (exception: IOException) {
            RCONServer.log.error("An exception happened while closing the RCON server", exception)
        }

        synchronized(this) {
            (this as Object).notify()
        }
    }

    @Throws(IOException::class)
    private fun read(key: SelectionKey) {
        val channel = key.channel() as SocketChannel
        val buffer = ByteBuffer.allocate(4096)
        buffer.order(ByteOrder.LITTLE_ENDIAN)

        val bytesRead: Int
        try {
            bytesRead = channel.read(buffer)
        } catch (exception: IOException) {
            key.cancel()
            channel.close()
            rconSessions.remove(channel)
            sendQueues.remove(channel)
            return
        }

        if (bytesRead == -1) {
            key.cancel()
            channel.close()
            rconSessions.remove(channel)
            sendQueues.remove(channel)
            return
        }

        buffer.flip()
        this.handle(channel, RCONPacket(buffer))
    }

    private fun handle(channel: SocketChannel, packet: RCONPacket) {
        when (packet.type) {
            SERVERDATA_AUTH -> {
                val payload = ByteArray(1)

                if (String(packet.payload, Charset.forName("UTF-8")) == this.password) {
                    rconSessions.add(channel)
                    this.send(channel, RCONPacket(packet.id, SERVERDATA_AUTH_RESPONSE, payload))
                    return
                }

                this.send(channel, RCONPacket(-1, SERVERDATA_AUTH_RESPONSE, payload))
            }

            SERVERDATA_EXECCOMMAND -> {
                if (!rconSessions.contains(channel)) {
                    return
                }

                val command = String(packet.payload, Charset.forName("UTF-8")).trim { it <= ' ' }
                synchronized(this.receiveQueue) {
                    receiveQueue.add(RCONCommand(channel, packet.id, command))
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun write(key: SelectionKey) {
        val channel = key.channel() as SocketChannel

        synchronized(this.sendQueues) {
            val queue = sendQueues[channel]!!
            val buffer = queue[0].toBuffer()
            try {
                channel.write(buffer)
                queue.removeAt(0)
            } catch (exception: IOException) {
                key.cancel()
                channel.close()
                rconSessions.remove(channel)
                sendQueues.remove(channel)
                return
            }

            if (queue.isEmpty()) {
                sendQueues.remove(channel)
            }
            key.interestOps(SelectionKey.OP_READ)
        }
    }

    private fun send(channel: SocketChannel, packet: RCONPacket) {
        if (!channel.keyFor(this.selector).isValid) {
            return
        }

        synchronized(this.sendQueues) {
            val queue = sendQueues.computeIfAbsent(channel) { k: SocketChannel? -> ArrayList() }
            queue.add(packet)
        }

        selector.wakeup()
    }

    companion object {
        private const val SERVERDATA_AUTH = 3
        private const val SERVERDATA_AUTH_RESPONSE = 2
        private const val SERVERDATA_EXECCOMMAND = 2
        private const val SERVERDATA_RESPONSE_VALUE = 0
    }
}
