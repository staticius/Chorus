package cn.nukkit.command.defaults

import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.utils.CommandLogger
import cn.nukkit.entity.Entity
import java.util.stream.Collectors

class TestForCommand(name: String) : VanillaCommand(name, "commands.testfor.description") {
    init {
        this.permission = "nukkit.command.testfor"
        getCommandParameters().clear()
        this.addCommandParameters(
            "default", arrayOf<CommandParameter?>(
                CommandParameter.Companion.newType("victim", false, CommandParamType.TARGET)
            )
        )
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList?>,
        log: CommandLogger
    ): Int {
        val targets = result.value!!.getResult<List<Entity>>(0)!!
        if (targets.isEmpty()) {
            log.addNoTargetMatch().output()
            return 0
        } else {
            log.addSuccess("commands.testfor.success", targets.stream().map<String> { entity: Entity ->
                var name = entity.name
                if (name.isBlank()) name = entity.originalName
                name
            }.collect(Collectors.joining(","))).output()
            return targets.size
        }
    }
}
