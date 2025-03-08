package cn.nukkit.command.defaults

import cn.nukkit.Player
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandEnum
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.tree.node.PlayersNode
import cn.nukkit.command.utils.CommandLogger
import cn.nukkit.level.Locator
import com.google.common.collect.Lists
import java.util.*

class PlaySoundCommand(name: String) : VanillaCommand(name, "commands.playsound.description") {
    init {
        this.permission = "nukkit.command.playsound"
        getCommandParameters().clear()
        this.addCommandParameters(
            "default",
            arrayOf<CommandParameter?>(
                CommandParameter.Companion.newEnum(
                    "sound",
                    false,
                    CommandEnum(
                        "sound",
                        Arrays.stream<Sound>(Sound.entries.toTypedArray()).map<String> { obj: Sound -> obj.getSound() }
                            .toList(),
                        true)),
                CommandParameter.Companion.newType("player", true, CommandParamType.TARGET, PlayersNode()),
                CommandParameter.Companion.newType("position", true, CommandParamType.POSITION),
                CommandParameter.Companion.newType("volume", true, CommandParamType.FLOAT),
                CommandParameter.Companion.newType("pitch", true, CommandParamType.FLOAT),
                CommandParameter.Companion.newType("minimumVolume", true, CommandParamType.FLOAT)
            ))
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList?>,
        log: CommandLogger
    ): Int {
        val list = result.value
        val sound = list!!.getResult<String>(0)
        var targets: List<Player>? = null
        var locator: Locator? = null
        var volume = 1f
        var pitch = 1f
        var minimumVolume = 0f
        if (list.hasResult(1)) targets = list.getResult(1)
        if (list.hasResult(2)) locator = list.getResult(2)
        if (list.hasResult(3)) volume = list.getResult(3)!!
        if (list.hasResult(4)) pitch = list.getResult(4)!!
        if (list.hasResult(5)) minimumVolume = list.getResult(5)!!
        if (minimumVolume < 0) {
            log.addNumTooSmall(5, 0).output()
            return 0
        }

        if (targets == null || targets.isEmpty()) {
            if (sender.isPlayer) {
                targets = Lists.newArrayList(sender.asPlayer())
            } else {
                log.addError("commands.generic.noTargetMatch").output()
                return 0
            }
        }
        if (locator == null) {
            locator = targets!![0].locator
        }

        val maxDistance = (if (volume > 1) volume * 16 else 16f).toDouble()

        val successes: MutableList<String> = Lists.newArrayList()
        for (player in targets!!) {
            val name = player.name
            val packet: PlaySoundPacket = PlaySoundPacket()
            if (locator.position.distance(player.position) > maxDistance) {
                if (minimumVolume <= 0) {
                    log.addError("commands.playsound.playerTooFar", name)
                    continue
                }

                packet.volume = minimumVolume
                packet.x = player.position.floorX
                packet.y = player.position.floorY
                packet.z = player.position.floorZ
            } else {
                packet.volume = volume
                packet.x = locator.position.floorX
                packet.y = locator.position.floorY
                packet.z = locator.position.floorZ
            }

            packet.name = sound
            packet.pitch = pitch
            player.dataPacket(packet)

            successes.add(name)
        }
        log.addSuccess("commands.playsound.success", sound, java.lang.String.join(", ", successes))
            .successCount(successes.size).output()
        return successes.size
    }
}
