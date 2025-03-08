package cn.nukkit.command.defaults

import cn.nukkit.command.Command
import cn.nukkit.command.data.CommandData

/**
 * 测试命令基类
 */
abstract class TestCommand @JvmOverloads constructor(
    name: String,
    description: String = "",
    usageMessage: String? = "",
    aliases: Array<String> = arrayOf()
) :
    Command(name, description, usageMessage, aliases) {
    init {
        //标记为测试命令（客户端显示为蓝色）
        commandData.flags.add(CommandData.Flag.TEST_USAGE)
    }
}
