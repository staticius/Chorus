package cn.nukkit.command.simple

import cn.nukkit.command.*
import cn.nukkit.command.Command
import cn.nukkit.lang.TranslationContainer
import lombok.extern.slf4j.Slf4j
import java.lang.reflect.Method

/**
 * @author Tee7even
 */
@Slf4j
class SimpleCommand(
    private val `object`: Any,
    private val method: Method,
    name: String,
    description: String,
    usageMessage: String?,
    aliases: Array<String>
) :
    Command(name, description, usageMessage, aliases) {
    private var forbidConsole = false
    private var maxArgs = 0
    private var minArgs = 0

    fun setForbidConsole(forbidConsole: Boolean) {
        this.forbidConsole = forbidConsole
    }

    fun setMaxArgs(maxArgs: Int) {
        this.maxArgs = maxArgs
    }

    fun setMinArgs(minArgs: Int) {
        this.minArgs = minArgs
    }

    fun sendUsageMessage(sender: CommandSender) {
        if (this.usageMessage != "") {
            sender.sendMessage(TranslationContainer("commands.generic.usage", this.usageMessage))
        }
    }

    fun sendInGameMessage(sender: CommandSender) {
        sender.sendMessage(TranslationContainer("nukkit.command.generic.ingame"))
    }

    override fun execute(sender: CommandSender, commandLabel: String?, args: Array<String?>): Boolean {
        if (this.forbidConsole && sender is ConsoleCommandSender) {
            this.sendInGameMessage(sender)
            return false
        } else if (!this.testPermission(sender)) {
            return false
        } else if (this.maxArgs != 0 && args.size > this.maxArgs) {
            this.sendUsageMessage(sender)
            return false
        } else if (this.minArgs != 0 && args.size < this.minArgs) {
            this.sendUsageMessage(sender)
            return false
        }

        var success = false

        try {
            success = method.invoke(this.`object`, sender, commandLabel, args) as Boolean
        } catch (exception: Exception) {
            SimpleCommand.log.error("Failed to execute {} by {}", commandLabel, sender.name, exception)
        }

        if (!success) {
            this.sendUsageMessage(sender)
        }

        return success
    }
}
