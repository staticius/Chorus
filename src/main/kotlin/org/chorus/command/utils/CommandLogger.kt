package org.chorus.command.utils

import org.chorus.Player
import org.chorus.Server
import org.chorus.blockentity.ICommandBlock
import org.chorus.command.*
import org.chorus.lang.CommandOutputContainer
import org.chorus.lang.PluginI18nManager
import org.chorus.lang.TranslationContainer
import org.chorus.level.GameRule
import org.chorus.network.protocol.types.CommandOutputMessage
import org.chorus.plugin.InternalPlugin
import org.chorus.plugin.Plugin
import org.chorus.plugin.PluginBase
import org.chorus.utils.TextFormat
import java.util.*
import kotlin.math.max


class CommandLogger(
    val command: Command,
    val sender: CommandSender,
    val commandLabel: String?,
    val args: Array<String?>,
    outputContainer: CommandOutputContainer?,
    val plugin: Plugin?
) {
    @JvmOverloads
    constructor(
        command: Command,
        sender: CommandSender,
        commandLabel: String?,
        args: Array<String?>,
        outputContainer: CommandOutputContainer? = CommandOutputContainer()
    ) : this(command, sender, commandLabel, args, outputContainer, InternalPlugin.INSTANCE)

    constructor(
        command: Command,
        sender: CommandSender,
        commandLabel: String?,
        args: Array<String?>,
        plugin: Plugin?
    ) : this(command, sender, commandLabel, args, CommandOutputContainer(), plugin)

    fun addSuccess(message: String): CommandLogger {
        return this.addSuccess(message, *CommandOutputContainer.EMPTY_STRING)
    }

    fun addSuccess(key: String, params: List<String?>): CommandLogger {
        return this.addSuccess(key, *params.toArray<String>(CommandOutputContainer.EMPTY_STRING))
    }

    /**
     * 添加一条命令成功执行的消息，参数可以是纯文本，也可以是客戶端的多语言文本key.<br></br>默认输出颜色白色
     *
     *
     * Add a message that the command was successfully executed, the parameters can be plain text or the client's multilingual text key.<br></br>Default output color white
     *
     * @param key    the key
     * @param params the params
     */
    fun addSuccess(key: String, vararg params: String?): CommandLogger {
        var key = key
        if (TextFormat.getLastColors(key).isEmpty()) {
            key =
                if (Server.getInstance().language.internalGet(key) != null) TextFormat.WHITE.toString() + "%" + key
                else TextFormat.WHITE.toString() + key
        }

        outputContainer.messages.add(CommandOutputMessage(key, *params))
        outputContainer.incrementSuccessCount()
        return this
    }

    /**
     * 添加一条命令错误执行的消息，参数可以是纯文本，也可以是客戶端的多语言文本key.<br></br>默认输出颜色红色
     *
     *
     * Add a command error message, either plain text or the client's multilingual text key.<br></br>Default output color red
     *
     * @param message the message
     * @return the command logger
     */
    fun addError(message: String): CommandLogger {
        return this.addError(message, *CommandOutputContainer.EMPTY_STRING)
    }

    /**
     * 添加一条命令执行失败的错误消息，参数可以是纯文本，也可以是客戶端的多语言文本key.<br></br>默认输出颜色红色
     *
     *
     * Add a command execution failure error message, either plain text or the client's multilingual text key.<br></br>Default output color red
     *
     * @param key    语言文本key/错误信息
     * @param params 语言文本参数/空
     * @return the command logger
     */
    fun addError(key: String, vararg params: String?): CommandLogger {
        outputContainer.messages.add(CommandOutputMessage(key, *params))
        return this
    }

    /**
     * 添加一条消息，参数可以是纯文本，也可以是客户端，服务端，以及[PluginI18n][org.chorus.lang.PluginI18n]中的多语言文本，默认输出颜色红色
     *
     *
     * Add a message, the parameters can be plain text, or client-side, server-side, and multilingual text in [PluginI18n][org.chorus.lang.PluginI18n], default output color red
     *
     * @param key the key
     * @return the command logger
     */
    fun addMessage(key: String?): CommandLogger {
        return this.addMessage(key, *CommandOutputContainer.EMPTY_STRING)
    }

    /**
     * 添加一条消息，参数可以是纯文本，也可以是客户端，服务端，以及[PluginI18n][org.chorus.lang.PluginI18n]中的多语言文本，默认输出颜色红色
     *
     *
     * Add a message, the parameters can be plain text, or client-side, server-side, and multilingual text in [PluginI18n][org.chorus.lang.PluginI18n], default output color red
     *
     * @param key    the key
     * @param params the params
     * @return the command logger
     */
    fun addMessage(key: String?, vararg params: String?): CommandLogger {
        if (plugin is PluginBase) {
            val i18n = PluginI18nManager.getI18n(plugin)
            if (i18n != null) {
                val text = if (sender.isPlayer) {
                    i18n.tr(sender.asPlayer()!!.languageCode, key, *params)
                } else {
                    i18n.tr(Server.getInstance().languageCode, key, *params)
                }
                outputContainer.messages.add(CommandOutputMessage(text, *CommandOutputContainer.EMPTY_STRING))
                return this
            }
        }
        outputContainer.messages.add(
            CommandOutputMessage(
                Server.getInstance().language.tr(key, *params),
                *CommandOutputContainer.EMPTY_STRING
            )
        )
        return this
    }

    /**
     * 添加一条默认的命令格式错误信息,会提示命令发送者在指定索引处发生错误
     *
     *
     * Add a default command format error message that will alert the command sender of an error at the specified index
     *
     * @param errorIndex 发生错误的参数索引
     */
    fun addSyntaxErrors(errorIndex: Int): CommandLogger {
        if (sender is ConsoleCommandSender) {
            this.addMessage(
                "commands.generic.usage", """
     
     ${command.commandFormatTips}
     """.trimIndent()
            )
        } else this.addError("commands.generic.syntax", *this.syntaxErrorsValue(errorIndex))
        return this
    }

    /**
     * 添加一条目标选择器没有匹配目标的错误信息
     *
     *
     * Add an error message that the target selector matches too many targets
     */
    fun addNoTargetMatch(): CommandLogger {
        this.addError("commands.generic.noTargetMatch", *CommandOutputContainer.EMPTY_STRING)
        return this
    }

    /**
     * 添加一条目标选择器匹配目标过多的错误信息
     */
    fun addTooManyTargets(): CommandLogger {
        this.addError("commands.generic.tooManyTargets", *CommandOutputContainer.EMPTY_STRING)
        return this
    }

    /**
     * 添加一条参数过小的错误信息，会提示命令发送者指定位置的参数最小值不能低于minimum
     *
     *
     * Add an error message that the parameter is too small, prompting the command sender to specify a location where the minimum value of the parameter cannot be less than minimum
     *
     * @param errorIndex 发生错误的参数索引
     * @param minimum    允许的最小值
     */
    fun addNumTooSmall(errorIndex: Int, minimum: Int): CommandLogger {
        this.addError("commands.generic.num.tooSmall", args[errorIndex], " $minimum")
        return this
    }

    /**
     * 添加一条Double参数过大的错误信息，会提示命令发送者指定位置的参数最大值不能超过maximum
     *
     *
     * Add a Double parameter too large error message, which will prompt the command sender to specify that the maximum value of the parameter at the location cannot exceed maximum
     *
     * @param errorIndex 发生错误的参数索引
     * @param maximum    允许的最大值
     */
    fun addDoubleTooBig(errorIndex: Int, maximum: Double): CommandLogger {
        this.addError("commands.generic.double.tooBig", args[errorIndex], " $maximum")
        return this
    }

    /**
     * 添加一条Double参数过小的错误信息，会提示命令发送者指定位置的参数最小值不能低于minimum
     *
     *
     * Add a Double parameter is too small error message, which will prompt the command sender to specify the minimum value of the parameter at the location cannot be less than minimum
     *
     * @param errorIndex 发生错误的参数索引
     * @param minimum    允许的最小值
     */
    fun addDoubleTooSmall(errorIndex: Int, minimum: Double): CommandLogger {
        this.addError("commands.generic.double.tooSmall", args[errorIndex], " $minimum")
        return this
    }

    /**
     * 添加一条无法访问世界外的方块的错误信息
     *
     *
     * Add an error message about not being able to access squares outside the world
     *
     * @return the command logger
     */
    fun addOutOfWorld(): CommandLogger {
        this.addError("commands.generic.outOfWorld", *CommandOutputContainer.EMPTY_STRING)
        return this
    }

    /**
     * 输出[.outputContainer]中的所有信息.
     *
     * @param broadcast the broadcast
     */
    /**
     * 输出[.outputContainer]中的所有信息.
     */
    @JvmOverloads
    fun output(broadcast: Boolean = false) {
        sender.sendCommandOutput(this.outputContainer)
        if (broadcast) {
            for (msg in outputContainer.messages) {
                broadcastAdminChannel(msg.messageId, msg.parameters)
            }
        }
        outputContainer.successCount = 0
        outputContainer.messages.clear()
    }

    /**
     * 标记[.outputContainer]的成功数量
     *
     * @param successCount the success count
     * @return the command logger
     */
    fun successCount(successCount: Int): CommandLogger {
        outputContainer.successCount = successCount
        return this
    }

    /**
     * 输出给指定目标一条反馈信息
     *
     *
     * Output a feedback message to the specified receiver
     *
     * @param receiver 命令目标
     * @param key      the key
     * @param params   给命令目标的反馈信息参数
     */
    fun outputObjectWhisper(receiver: Player, key: String?, vararg params: String?) {
        if (receiver.level.gameRules.getBoolean(GameRule.SEND_COMMAND_FEEDBACK)) {
            receiver.sendMessage(TranslationContainer(key, *params))
        }
    }

    /**
     * 输出给指定目标一条反馈信息
     *
     *
     * Output a feedback message to the specified receiver
     *
     * @param rawtext  给命令目标的反馈信息
     * @param receiver 命令目标
     * @param params   给命令目标的反馈信息参数
     */
    fun outputObjectWhisper(receiver: Player, rawtext: String, vararg params: Any?) {
        if (receiver.level.gameRules.getBoolean(GameRule.SEND_COMMAND_FEEDBACK)) {
            receiver.sendRawTextMessage(org.chorus.command.utils.RawText.Companion.fromRawText(String.format(rawtext, *params)))
        }
    }

    private fun syntaxErrorsValue(errorIndex: Int): Array<String?> {
        val join1 = StringJoiner(" ", "", " ")
        join1.add(commandLabel)

        if (errorIndex == -1) {
            val result = join1.toString()
            return arrayOf(
                result.substring(max(0.0, (result.length - SYNTAX_ERROR_LENGTH_LIMIT).toDouble()).toInt()),
                " ",
                " "
            )
        } else if (errorIndex == args.size) {
            Arrays.stream(args).forEach { newElement: String? -> join1.add(newElement) }
            val result = join1.toString()
            return arrayOf(
                result.substring(max(0.0, (result.length - SYNTAX_ERROR_LENGTH_LIMIT).toDouble()).toInt()),
                "",
                ""
            )
        }

        for (i in 0..<errorIndex) {
            join1.add(args[i])
        }
        val join2 = StringJoiner(" ", " ", "")
        var i = errorIndex + 1
        val len = args.size
        while (i < len) {
            join2.add(args[i])
            ++i
        }

        val end = args[errorIndex] + join2
        if (end.length >= SYNTAX_ERROR_LENGTH_LIMIT) {
            return arrayOf("", args[errorIndex], join2.toString())
        } else {
            val result = join1.toString()
            return arrayOf(
                result.substring(
                    max(
                        0.0,
                        (join1.length() + end.length - SYNTAX_ERROR_LENGTH_LIMIT).toDouble()
                    ).toInt()
                ),
                args[errorIndex], join2.toString()
            )
        }
    }

    private fun broadcastAdminChannel(key: String, value: Array<String>) {
        var target = sender
        if (target is ExecutorCommandSender) target = target.executor!!
        if (target is ICommandBlock) return
        val message = broadcastMessage(key, value, target)

        val users = target.server.pluginManager.getPermissionSubscriptions(Server.BROADCAST_CHANNEL_ADMINISTRATIVE)
        users.remove(target)
        for (user in users) {
            if (user is CommandSender) {
                user.sendMessage(message)
            }
        }
    }

    private fun broadcastMessage(key: String, value: Array<String>, target: CommandSender): TranslationContainer {
        val message = TranslationContainer(TextFormat.clean(key), *value)
        val resultStr =
            "[" + target.name + ": " + (if (message.text != target.server.language[message.text]) "%" else "") + message.text + "]"
        val coloredStr = TextFormat.GRAY.toString() + "" + TextFormat.ITALIC + resultStr
        message.text = coloredStr
        return message
    }

    val outputContainer: CommandOutputContainer = outputContainer!!

    companion object {
        private const val SYNTAX_ERROR_LENGTH_LIMIT: Byte = 23
    }
}
