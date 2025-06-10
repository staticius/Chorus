package org.chorus_oss.chorus.command.defaults

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.command.CommandSender
import org.chorus_oss.chorus.command.data.CommandParamType
import org.chorus_oss.chorus.command.data.CommandParameter
import org.chorus_oss.chorus.command.tree.ParamList
import org.chorus_oss.chorus.command.tree.node.PlayersNode
import org.chorus_oss.chorus.command.utils.CommandLogger
import org.chorus_oss.chorus.network.protocol.CameraShakePacket
import java.util.function.Consumer
import java.util.stream.Collectors

class CameraShakeCommand(name: String) : VanillaCommand(name, "commands.screenshake.description") {
    init {
        this.permission = "chorus.command.camerashake"
        commandParameters.clear()
        commandParameters["add"] = arrayOf(
            CommandParameter.Companion.newEnum("add", false, arrayOf("add")),
            CommandParameter.Companion.newType("player", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newType("intensity", false, CommandParamType.FLOAT),
            CommandParameter.Companion.newType("second", false, CommandParamType.FLOAT),
            CommandParameter.Companion.newEnum(
                "shakeType",
                false,
                arrayOf("positional", "rotational")
            )
        )
        commandParameters["stop"] = arrayOf(
            CommandParameter.Companion.newEnum("stop", false, arrayOf("stop")),
            CommandParameter.Companion.newType("player", false, CommandParamType.TARGET, PlayersNode()),
        )
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList>,
        log: CommandLogger
    ): Int {
        val list = result.value
        val players = list.getResult<List<Player>>(1)!!
        if (players.isEmpty()) {
            log.addNoTargetMatch().output()
            return 0
        }
        when (result.key) {
            "add" -> {
                val players_str = players.stream().map { obj: Player -> obj.name }.collect(Collectors.joining(" "))
                val intensity = list.getResult<Float>(2)!!
                val second = list.getResult<Float>(3)!!
                val shakeType = when (val type = list.getResult<String>(4)) {
                    "positional" -> CameraShakePacket.CameraShakeType.POSITIONAL
                    "rotational" -> CameraShakePacket.CameraShakeType.ROTATIONAL
                    else -> throw RuntimeException("Unknown CameraShakeType: $type")
                }
                val packet = CameraShakePacket(
                    intensity = intensity,
                    seconds = second,
                    shakeType = shakeType,
                    shakeAction = CameraShakePacket.CameraShakeAction.ADD
                )
                players.forEach(Consumer { player: Player -> player.dataPacket(packet) })
                log.addSuccess("commands.screenshake.success", players_str).output()
                return 1
            }

            "stop" -> {
                val playersStr = players.stream().map { obj: Player -> obj.name }.collect(Collectors.joining(" "))
                val packet = CameraShakePacket(
                    intensity = -1f,
                    seconds = -1f,
                    shakeType = CameraShakePacket.CameraShakeType.POSITIONAL,
                    shakeAction = CameraShakePacket.CameraShakeAction.STOP
                )
                players.forEach(Consumer { player: Player -> player.dataPacket(packet) })
                log.addSuccess("commands.screenshake.successStop", playersStr).output()
                return 1
            }

            else -> {
                return 0
            }
        }
    }
}
