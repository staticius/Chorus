package org.chorus.command.defaults

import org.chorus.Player
import org.chorus.command.CommandSender
import org.chorus.command.data.CommandParamType
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.tree.node.PlayersNode
import org.chorus.command.utils.CommandLogger
import java.util.function.Consumer
import java.util.stream.Collectors
import kotlin.collections.set

class CameraShakeCommand(name: String) : VanillaCommand(name, "commands.screenshake.description") {
    init {
        this.permission = "nukkit.command.camerashake"
        commandParameters.clear()
        commandParameters["add"] = arrayOf(
            CommandParameter.Companion.newEnum("add", false, arrayOf<String?>("add")),
            CommandParameter.Companion.newType("player", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newType("intensity", false, CommandParamType.FLOAT),
            CommandParameter.Companion.newType("second", false, CommandParamType.FLOAT),
            CommandParameter.Companion.newEnum(
                "shakeType",
                false,
                arrayOf<String?>("positional", "rotational")
            )
        )
        commandParameters["stop"] = arrayOf(
            CommandParameter.Companion.newEnum("stop", false, arrayOf<String?>("stop")),
            CommandParameter.Companion.newType("player", false, CommandParamType.TARGET, PlayersNode()),
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
        val players = list!!.getResult<List<Player>>(1)!!
        if (players.isEmpty()) {
            log.addNoTargetMatch().output()
            return 0
        }
        when (result.key) {
            "add" -> {
                val players_str = players.stream().map { obj: Player -> obj.name }.collect(Collectors.joining(" "))
                val intensity = list.getResult<Float>(2)!!
                val second = list.getResult<Float>(3)!!
                val type = list.getResult<String>(4)
                val shakeType: CameraShakeType? = when (type) {
                    "positional" -> CameraShakePacket.CameraShakeType.POSITIONAL
                    "rotational" -> CameraShakePacket.CameraShakeType.ROTATIONAL
                    else -> null
                }
                val packet: CameraShakePacket = CameraShakePacket()
                packet.intensity = intensity
                packet.duration = second
                packet.shakeType = shakeType
                packet.shakeAction = CameraShakePacket.CameraShakeAction.ADD
                players.forEach(Consumer { player: Player -> player.dataPacket(packet) })
                log.addSuccess("commands.screenshake.success", players_str).output()
                return 1
            }

            "stop" -> {
                val players_str = players.stream().map { obj: Player -> obj.name }.collect(Collectors.joining(" "))
                val packet: CameraShakePacket = CameraShakePacket()
                packet.shakeAction = CameraShakePacket.CameraShakeAction.STOP
                //avoid NPE
                packet.intensity = -1f
                packet.duration = -1f
                packet.shakeType = CameraShakePacket.CameraShakeType.POSITIONAL
                players.forEach(Consumer { player: Player -> player.dataPacket(packet) })
                log.addSuccess("commands.screenshake.successStop", players_str).output()
                return 1
            }

            else -> {
                return 0
            }
        }
    }
}
