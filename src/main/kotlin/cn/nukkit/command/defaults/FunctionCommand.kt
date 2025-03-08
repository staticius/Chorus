package cn.nukkit.command.defaults

import cn.nukkit.Server
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandEnum
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.utils.CommandLogger
import kotlin.collections.Map
import kotlin.collections.set

class FunctionCommand(name: String) : VanillaCommand(name, "commands.function.description") {
    init {
        this.permission = "nukkit.command.function"
        commandParameters.clear()
        commandParameters["default"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum(
                "name",
                false,
                CommandEnum.Companion.FUNCTION_FILE
            ) //todo 找到CommandParamType.FILE_PATH自动补全的工作原理
        )
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList?>,
        log: CommandLogger
    ): Int {
        val list = result.value
        if (result.key == "default") {
            val file = list!!.getResult<String>(0)
            val function = Server.getInstance().functionManager.getFunction(file)
            if (function == null) {
                log.addError("commands.function.functionNameNotFound", file).output()
                return 0
            }
            function.dispatch(sender)
            log.addSuccess("commands.function.success", function.commands.size.toString()).output()
            return 1
        }
        return 0
    }
}
