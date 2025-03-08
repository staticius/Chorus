package cn.nukkit.network.rcon

import java.nio.channels.SocketChannel

/**
 * A data structure to hold sender, request ID and command itself.
 *
 * @author Tee7even
 */
class RCONCommand(val sender: SocketChannel, val id: Int, val command: String)
