package cn.nukkit.command.defaults

import cn.nukkit.Player
import cn.nukkit.camera.instruction.impl.ClearInstruction.get
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.utils.CommandLogger
import cn.nukkit.entity.Entity
import cn.nukkit.utils.TextFormat
import java.util.stream.Collectors

/**
 * @author Pub4Game
 * @since 2015/12/08
 */
class KillCommand(name: String) : VanillaCommand(name, "commands.kill.description") {
    init {
        this.permission = ("nukkit.command.kill.self;"
                + "nukkit.command.kill.other")
        commandParameters.clear()
        commandParameters["default"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("player", true, CommandParamType.TARGET)
        )
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList?>,
        log: CommandLogger
    ): Int {
        if (result.value!!.hasResult(0)) {
            if (!sender.hasPermission("nukkit.command.kill.other")) {
                log.addError("nukkit.command.generic.permission").output()
                return 0
            }
            var entities = result.value!!.getResult<MutableList<Entity>>(0)!!
            entities.removeIf { entity: Entity -> !entity.isAlive }
            if (entities.isEmpty()) {
                log.addNoTargetMatch().output()
                return 0
            }
            val creativePlayer: AtomicBoolean = AtomicBoolean(false)
            entities = entities.stream().filter { entity: Entity ->
                if (entity is Player) if (entity.isCreative) {
                    creativePlayer.set(true)
                    return@filter false.toInt()
                } else return@filter true.toInt()
                else return@filter true.toInt()
            }.toList()

            if (entities.isEmpty()) {
                if (creativePlayer.get()) log.addError(TextFormat.WHITE.toString() + "%commands.kill.attemptKillPlayerCreative")
                else log.addNoTargetMatch()
                log.output()
                return 0
            }

            for (entity in entities) {
                if (entity.name == sender.name) {
                    if (!sender.hasPermission("nukkit.command.kill.self")) {
                        continue
                    }
                }
                if (entity is Player) {
                    val ev: EntityDamageEvent = EntityDamageEvent(entity, DamageCause.SUICIDE, 1000000f)
                    entity.attack(ev)
                } else {
                    entity.kill()
                }
            }
            val message = entities.stream().map { obj: Entity -> obj.name }.collect(Collectors.joining(", "))
            log.addSuccess("commands.kill.successful", message).successCount(entities.size).output(true)
            return entities.size
        } else {
            if (sender.isPlayer) {
                if (!sender.hasPermission("nukkit.command.kill.self")) {
                    log.addError("nukkit.command.generic.permission").output()
                    return 0
                }
                if (sender.asPlayer()!!.isCreative) {
                    log.addError("commands.kill.attemptKillPlayerCreative").output()
                    return 0
                }
                val ev: EntityDamageEvent = EntityDamageEvent(sender.asPlayer(), DamageCause.SUICIDE, 1000000f)
                sender.asPlayer().attack(ev)
            } else {
                log.addError(
                    "commands.generic.usage", """
     
     ${this.commandFormatTips}
     """.trimIndent()
                ).output()
                return 0
            }
            return 1
        }
    }
}
