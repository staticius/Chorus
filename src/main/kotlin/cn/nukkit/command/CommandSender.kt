package cn.nukkit.command

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.entity.Entity
import cn.nukkit.lang.CommandOutputContainer
import cn.nukkit.level.Locator
import cn.nukkit.level.Transform
import cn.nukkit.permission.Permissible

/**
 * 能发送命令的对象.<br></br>
 * 可以是一个玩家或者一个控制台或者一个实体或者其他.
 *
 *
 * Who sends commands.<br></br>
 * That can be a player or a console.
 *
 * @author MagicDroidX(code) @ Nukkit Project
 * @author 粉鞋大妈(javadoc) @ Nukkit Project
 * @author smartcmd(code) @ PowerNukkitX Project
 * @see cn.nukkit.command.CommandExecutor.onCommand
 *
 * @since Nukkit 1.0 | Nukkit API 1.0.0
 */
interface CommandSender : Permissible {
    /**
     * 给命令发送者返回信息.
     *
     *
     * Sends a message to the command sender.
     *
     * @param message 要发送的信息.<br></br>Message to send.
     * @see cn.nukkit.utils.TextFormat
     *
     * @since Nukkit 1.0 | Nukkit API 1.0.0
     */
    fun sendMessage(message: String)

    /**
     * 给命令发送者返回信息.
     *
     *
     * Sends a message to the command sender.
     *
     * @param message 要发送的信息.<br></br>Message to send.
     * @since Nukkit 1.0 | Nukkit API 1.0.0
     */
    fun sendMessage(message: TextContainer)

    /**
     * Send command output.
     *
     * @param container the container
     */
    fun sendCommandOutput(container: CommandOutputContainer)

    /**
     * 返回命令发送者所在的服务器.
     *
     *
     * Returns the server of the command sender.
     *
     * @return 命令发送者所在的服务器.<br></br>the server of the command sender.
     * @since Nukkit 1.0 | Nukkit API 1.0.0
     */
    val server: Server?

    /**
     * 返回命令发送者的名称.<br></br>
     * 如果命令发送者是一个玩家，将会返回他的玩家名字(name)不是显示名字(display name).<br></br>
     * 如果命令发送者是控制台，将会返回`"CONSOLE"`.<br></br>
     * 当你需要判断命令的执行者是不是控制台时，可以用这个：<br></br>
     * `if(sender instanceof ConsoleCommandSender) .....;`
     *
     *
     * Returns the name of the command sender.<br></br>
     * If this command sender is a player, will return his/her player name(not display name).<br></br>
     * If it is a console, will return `"CONSOLE"`.<br></br>
     * When you need to determine if the sender is a console, use this:<br></br>
     * `if(sender instanceof ConsoleCommandSender) .....;`
     *
     * @return 命令发送者的名称.<br></br>the name of the command sender.
     * @see cn.nukkit.Player.getName
     * @see cn.nukkit.command.ConsoleCommandSender.getName
     * @see cn.nukkit.plugin.PluginDescription
     *
     * @since Nukkit 1.0 | Nukkit API 1.0.0
     */
    val name: String

    /**
     * @return 发送者是否为玩家<br></br>whether the sender is an player
     */
    val isPlayer: Boolean

    val isEntity: Boolean
        /**
         * 请使用这个方法来检查发送者是否是一个实体，而不是使用代码`"xxx instanceof Entity"`.<br></br>
         * 因为发送者可能不是`"Entity"`的一个实例，但实际上它是以一个实体的身份执行命令(例如：`"ExecutorCommandSender"`)
         *
         *
         * please use this method to check whether the sender is an entity instead of using code `"xxx instanceof Entity"` <br></br>
         * because the sender may not an instance of `"Entity"` but in fact it is executing commands identity as an entity(eg: `"ExecutorCommandSender"`)
         *
         * @return 发送者是否为实体<br></br>whether the sender is an entity
         */
        get() = false

    /**
     * 如果发送者是一个实体，返回执行该命令的实体.
     *
     *
     * return the entity who execute the command if the sender is a entity.
     *
     * @return 实体对象<br></br>Entity instance
     */
    fun asEntity(): Entity? {
        return null
    }

    /**
     * 如果发送者是一个玩家，返回执行该命令的玩家.
     *
     *
     * return the player who execute the command if the sender is a player.
     *
     * @return 玩家对象<br></br>Player instance
     */
    fun asPlayer(): Player? {
        return null
    }

    val locator: Locator
        /**
         * @return 返回发送者的Position<br></br>return the sender's position.
         */
        get() = Locator(0.0, 0.0, 0.0, Server.getInstance().defaultLevel)

    val transform: Transform
        /**
         * @return 返回发送者克隆过的Location<br></br>return the sender's location.
         */
        get() = Transform(0.0, 0.0, 0.0, Server.getInstance().defaultLevel)
}
