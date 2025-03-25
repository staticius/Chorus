package org.chorus.command.defaults

import org.chorus.Player
import org.chorus.Server
import org.chorus.command.CommandSender
import org.chorus.command.data.CommandParamType
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.tree.node.PlayersNode
import org.chorus.command.utils.CommandLogger
import org.chorus.network.protocol.StopSoundPacket
import java.util.stream.Collectors

class StopSoundCommand(name: String) : VanillaCommand(name, "commands.stopsound.description") {
    init {
        this.permission = "nukkit.command.stopsound"
        commandParameters.clear()
        this.addCommandParameters(
            "default", arrayOf(
                CommandParameter.Companion.newType("player", false, CommandParamType.TARGET, PlayersNode()),
                CommandParameter.Companion.newType("sound", true, CommandParamType.STRING)
            )
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
        val targets = list!!.getResult<List<Player>>(0)!!
        if (targets.isEmpty()) {
            log.addNoTargetMatch().output()
            return 0
        }
        var sound: String? = ""

        if (list.hasResult(1)) {
            sound = list.getResult(1)
        }
        val packet: StopSoundPacket = StopSoundPacket()
        packet.name = sound
        if (sound!!.isEmpty()) {
            packet.stopAll = true
        }
        Server.broadcastPacket(targets, packet)
        val players_str = targets.stream().map { obj: Player -> obj.name }.collect(Collectors.joining(" "))
        if (packet.stopAll) {
            log.addSuccess("commands.stopsound.success.all", players_str).output()
        } else {
            log.addSuccess("commands.stopsound.success", sound, players_str).output()
        }
        return 1
    }
}
