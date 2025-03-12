package org.chorus.command.defaults

import org.chorus.Player
import org.chorus.command.CommandSender
import org.chorus.command.data.CommandEnum
import org.chorus.command.data.CommandParamType
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.tree.node.PlayersNode
import org.chorus.command.utils.CommandLogger
import org.chorus.network.protocol.PlayerFogPacket
import org.chorus.utils.Identifier
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer
import kotlin.collections.set

class FogCommand(name: String) : VanillaCommand(name, "commands.fog.description", "commands.fog.usage") {
    init {
        this.permission = "nukkit.command.fog"
        commandParameters.clear()
        commandParameters["push"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("victim", CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("push", arrayOf<String?>("push")),
            CommandParameter.Companion.newType("fogId", CommandParamType.STRING),
            CommandParameter.Companion.newType("userProvidedId", CommandParamType.STRING)
        )
        commandParameters["delete"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("victim", CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("mode", CommandEnum("delete", "pop", "remove")),
            CommandParameter.Companion.newType("userProvidedId", CommandParamType.STRING)
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
        val targets = list!!.getResult<List<Player>>(0)!!
        if (targets.isEmpty()) {
            log.addNoTargetMatch().output()
            return 0
        }
        when (result.key) {
            "push" -> {
                val fogIdStr = list.getResult<String>(2)
                val fogId = Identifier.tryParse(fogIdStr)
                if (fogId == null) {
                    log.addError("commands.fog.invalidFogId", fogIdStr).output()
                    return 0
                }
                val userProvidedId = list.getResult<String>(3)
                val fog = PlayerFogPacket.Fog(fogId, userProvidedId)
                targets.forEach(Consumer<Player> { player: Player ->
                    player.fogStack.add(fog)
                    player.sendFogStack() //刷新到客户端
                })
                log.addSuccess("commands.fog.success.push", userProvidedId, fogIdStr).output()
                return 1
            }

            "delete" -> {
                val mode = list.getResult<String>(1)
                val userProvidedId = list.getResult<String>(2)
                val success = AtomicInteger(1)
                when (mode) {
                    "pop" -> {
                        targets.forEach(Consumer<Player> { player: Player ->
                            val fogStack = player.fogStack
                            for (i in fogStack.indices.reversed()) {
                                val fog = fogStack[i]
                                if (fog.userProvidedId == userProvidedId) {
                                    fogStack.removeAt(i)
                                    player.sendFogStack() //刷新到客户端
                                    log.addSuccess(
                                        "commands.fog.success.pop",
                                        userProvidedId,
                                        fog.identifier.toString()
                                    ).output()
                                    return@forEach
                                }
                            }
                            log.addError("commands.fog.invalidUserId", userProvidedId).output()
                            success.set(0)
                        })
                        return success.get()
                    }

                    "remove" -> {
                        targets.forEach(Consumer<Player> { player: Player ->
                            val fogStack = player.fogStack
                            val shouldRemoved: MutableList<PlayerFogPacket.Fog> = ArrayList()
                            for (i in fogStack.indices) {
                                val fog = fogStack[i]
                                if (fog.userProvidedId == userProvidedId) {
                                    shouldRemoved.add(fog)
                                    log.addSuccess(
                                        "commands.fog.success.remove",
                                        userProvidedId,
                                        fog.identifier.toString()
                                    ).output()
                                }
                            }
                            fogStack.removeAll(shouldRemoved)
                            player.sendFogStack() //刷新到客户端
                            if (shouldRemoved.size == 0) {
                                log.addError("commands.fog.invalidUserId", userProvidedId).output()
                                success.set(0)
                            }
                        })
                        return success.get()
                    }

                    else -> {
                        return 0
                    }
                }
            }

            else -> {
                return 0
            }
        }
    }
}
