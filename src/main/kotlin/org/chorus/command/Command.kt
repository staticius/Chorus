package org.chorus.command

import org.chorus.Player
import org.chorus.Server
import org.chorus.blockentity.ICommandBlock
import org.chorus.command.data.*
import org.chorus.command.tree.ParamList
import org.chorus.command.tree.ParamTree
import org.chorus.command.utils.CommandLogger
import org.chorus.lang.CommandOutputContainer
import org.chorus.lang.PluginI18nManager
import org.chorus.lang.TranslationContainer
import org.chorus.level.GameRule
import org.chorus.plugin.InternalPlugin
import org.chorus.plugin.Plugin
import org.chorus.plugin.PluginBase
import org.chorus.utils.TextFormat
import io.netty.util.internal.EmptyArrays
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.component1
import kotlin.collections.dropLastWhile
import kotlin.collections.forEach
import kotlin.collections.set
import kotlin.collections.toTypedArray
import kotlin.math.min


abstract class Command @JvmOverloads constructor(
    name: String,
    description: String = "",
    usageMessage: String? = null,
    aliases: Array<String> = EmptyArrays.EMPTY_STRINGS
) {
    @JvmField
    val name: String

    private var nextLabel: String?

    var label: String?
        private set

    private var aliases: Array<String>

    private var activeAliases: Array<String>

    private var commandMap: CommandMap? = null

    @JvmField
    var description: String

    var usage: String

    @JvmField
    var permission: String? = null

    @JvmField
    var permissionMessage: String? = null

    var commandParameters: MutableMap<String, Array<CommandParameter>> = HashMap()


    var paramTree: ParamTree? = null
        protected set

    /**
     * Returns an CommandData containing command data
     *
     * @return CommandData
     */
    var defaultCommandData: CommandData
        protected set

    var isServerSideOnly: Boolean = false
        protected set

    init {
        this.defaultCommandData = CommandData()
        this.name = name.lowercase() // Uppercase letters crash the client?!?
        this.nextLabel = name
        this.label = name
        this.description = description
        this.usage = usageMessage ?: "/$name"
        this.aliases = aliases
        this.activeAliases = aliases
        commandParameters["default"] =
            arrayOf<CommandParameter>(
                CommandParameter.Companion.newType(
                    "args",
                    true,
                    CommandParamType.RAWTEXT
                )
            )
    }

    fun getCommandParameters(key: String): Array<CommandParameter> {
        return commandParameters[key]!!
    }

    fun getCommandParameters(): Map<String, Array<CommandParameter>> {
        return commandParameters
    }

    fun setCommandParameters(commandParameters: MutableMap<String, Array<CommandParameter>>) {
        this.commandParameters = commandParameters
    }

    fun addCommandParameters(key: String, parameters: Array<CommandParameter>) {
        commandParameters[key] = parameters
    }

    /**
     * Generates modified command data for the specified player
     * for AvailableCommandsPacket.
     *
     * @param player player
     * @return CommandData|null
     */
    fun generateCustomCommandData(player: Player): CommandDataVersions? {
        if (!this.testPermission(player)) {
            return null
        }

        val plugin: Plugin = if (this is PluginCommand<*>) pluginCommand.getPlugin() else InternalPlugin.INSTANCE

        val customData = defaultCommandData.clone()

        if (getAliases().length > 0) {
            val aliases: MutableList<String?> = ArrayList(Arrays.asList(*getAliases()))
            if (!aliases.contains(this.name)) {
                aliases.add(this.name)
            }

            customData.aliases = CommandEnum(this.name + "Aliases", aliases)
        }

        if (plugin === InternalPlugin.INSTANCE) {
            customData.description =
                Server.instance.baseLang.tr(this.description, CommandOutputContainer.EMPTY_STRING, "commands.", false)
        } else if (plugin is PluginBase) {
            val i18n = PluginI18nManager.getI18n(plugin)
            if (i18n != null) {
                customData.description = i18n.tr(player.languageCode, this.description)
            } else {
                customData.description = Server.instance.baseLang.tr(this.description)
            }
        }

        commandParameters.forEach { (key: String?, params: Array<CommandParameter?>?) ->
            val overload = CommandOverload()
            overload.input.parameters = params
            customData.overloads[key] = overload
        }

        if (customData.overloads.isEmpty()) {
            customData.overloads["default"] = CommandOverload()
        }

        val versions: CommandDataVersions = CommandDataVersions()
        versions.versions.add(customData)
        return versions
    }

    val overloads: Map<String?, CommandOverload?>
        get() = defaultCommandData.overloads

    open fun execute(sender: CommandSender, commandLabel: String?, args: Array<String?>): Boolean {
        throw UnsupportedOperationException()
    }

    /**
     * Execute int.
     *
     * @param sender       命令发送者
     * @param commandLabel the command label
     * @param result       解析的命令结果
     * @param log          命令输出工具
     * @return int 返回0代表执行失败, 返回大于等于1代表执行成功
     */
    open fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList?>,
        log: CommandLogger
    ): Int {
        throw UnsupportedOperationException()
    }

    fun testPermission(target: CommandSender): Boolean {
        if (this.testPermissionSilent(target)) {
            return true
        }

        if (this.permissionMessage == null) {
            target.sendMessage(
                TranslationContainer(
                    TextFormat.RED.toString() + "%commands.generic.unknown",
                    this.name
                )
            )
        } else if (!permissionMessage!!.isEmpty()) {
            target.sendMessage(
                permissionMessage!!.replace(
                    "<permission>",
                    permission!!
                )
            )
        }

        return false
    }

    open fun testPermissionSilent(target: CommandSender): Boolean {
        if (this.permission == null || permission!!.isEmpty()) {
            return true
        }

        val permissions =
            permission!!.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (permission in permissions) {
            if (target.hasPermission(permission)) {
                return true
            }
        }

        return false
    }

    fun setLabel(name: String?): Boolean {
        this.nextLabel = name
        if (!this.isRegistered) {
            this.label = name
            return true
        }
        return false
    }

    fun register(commandMap: CommandMap): Boolean {
        if (this.allowChangesFrom(commandMap)) {
            this.commandMap = commandMap
            return true
        }
        return false
    }

    fun unregister(commandMap: CommandMap): Boolean {
        if (this.allowChangesFrom(commandMap)) {
            this.commandMap = null
            this.activeAliases = this.aliases
            this.label = this.nextLabel
            return true
        }
        return false
    }

    fun allowChangesFrom(commandMap: CommandMap): Boolean {
        return !isRegistered || this.commandMap == commandMap
    }

    val isRegistered: Boolean
        get() = this.commandMap != null

    fun getAliases(): Array<String> {
        return this.activeAliases
    }

    val commandFormatTips: String
        get() {
            val builder = StringBuilder()
            for (form in getCommandParameters().keys) {
                val commandParameters =
                    getCommandParameters()[form]!!
                builder.append("- /" + this.name)
                for (commandParameter in commandParameters) {
                    if (!commandParameter.optional) {
                        if (commandParameter.enumData == null) {
                            builder.append(" <")
                                .append(commandParameter.name + ": " + commandParameter.type!!.name.lowercase())
                                .append(">")
                        } else {
                            builder.append(" <").append(
                                commandParameter.enumData.values!!.subList(
                                    0,
                                    min(commandParameter.enumData.values!!.size.toDouble(), 10.0)
                                        .toInt()
                                ).stream().collect(Collectors.joining("|"))
                            ).append(if (commandParameter.enumData.values!!.size > 10) "|..." else "").append(">")
                        }
                    } else {
                        if (commandParameter.enumData == null) {
                            builder.append(" [")
                                .append(commandParameter.name + ": " + commandParameter.type!!.name.lowercase())
                                .append("]")
                        } else {
                            builder.append(" [").append(
                                commandParameter.enumData.values!!.subList(
                                    0,
                                    min(commandParameter.enumData.values!!.size.toDouble(), 10.0)
                                        .toInt()
                                ).stream().collect(Collectors.joining("|"))
                            ).append(if (commandParameter.enumData.values!!.size > 10) "|..." else "").append("]")
                        }
                    }
                }
                builder.append("\n")
            }
            return builder.toString()
        }

    fun setAliases(aliases: Array<String>) {
        this.aliases = aliases
        if (!this.isRegistered) {
            this.activeAliases = aliases
        }
    }

    fun hasParamTree(): Boolean {
        return this.paramTree != null
    }

    /**
     * 若调用此方法，则将启用ParamTree用于解析命令参数
     */
    fun enableParamTree() {
        this.paramTree = ParamTree(this)
    }

    override fun toString(): String {
        return this.name
    }

    companion object {
        @JvmOverloads
        fun broadcastCommandMessage(source: CommandSender, message: String?, sendToSource: Boolean = true) {
            val users = source.server.pluginManager.getPermissionSubscriptions(Server.BROADCAST_CHANNEL_ADMINISTRATIVE)

            val result = TranslationContainer("chat.type.admin", source.name, message)

            val colored = TranslationContainer(
                TextFormat.GRAY.toString() + "" + TextFormat.ITALIC + "%chat.type.admin",
                source.name,
                message
            )

            if (sendToSource && source !is ConsoleCommandSender) {
                source.sendMessage(message)
            }

            for (user in users) {
                if (user is CommandSender) {
                    if (user is ConsoleCommandSender) {
                        user.sendMessage(result)
                    } else if (user != source) {
                        user.sendMessage(colored)
                    }
                }
            }
        }

        @JvmOverloads
        fun broadcastCommandMessage(source: CommandSender, message: TextContainer, sendToSource: Boolean = true) {
            if ((source is ICommandBlock && !source.getLocator().getLevel().getGameRules()
                    .getBoolean(GameRule.COMMAND_BLOCK_OUTPUT)) ||
                (source is ExecutorCommandSender && source.executor is ICommandBlock && !source.getLocator().getLevel()
                    .getGameRules().getBoolean(GameRule.COMMAND_BLOCK_OUTPUT))
            ) {
                return
            }

            val m: TextContainer = message.clone()
            val resultStr =
                "[" + source.name + ": " + (if (m.getText() != source.server.baseLang.m.getText()]) "%" else "") + m.getText() + "]"

            val users = source.server.pluginManager.getPermissionSubscriptions(Server.BROADCAST_CHANNEL_ADMINISTRATIVE)

            val coloredStr = TextFormat.GRAY.toString() + "" + TextFormat.ITALIC + resultStr

            m.setText(resultStr)
            val result: TextContainer = m.clone()
            m.setText(coloredStr)
            val colored: TextContainer = m.clone()

            if (sendToSource && source !is ConsoleCommandSender) {
                source.sendMessage(message)
            }

            for (user in users) {
                if (user is CommandSender) {
                    if (user is ConsoleCommandSender) {
                        user.sendMessage(result)
                    } else if (user != source) {
                        user.sendMessage(colored)
                    }
                }
            }
        }
    }
}
