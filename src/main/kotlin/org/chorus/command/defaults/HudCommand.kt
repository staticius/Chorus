package org.chorus.command.defaults

import org.chorus.Player
import org.chorus.command.CommandSender
import org.chorus.command.data.CommandParamType
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.tree.node.PlayersNode
import org.chorus.command.utils.CommandLogger
import org.chorus.network.protocol.SetHudPacket
import org.chorus.network.protocol.types.hud.HudElement
import org.chorus.network.protocol.types.hud.HudVisibility

class HudCommand(name: String) : VanillaCommand(name, "commands.hud.description", "%commands.hud.usage") {
    init {
        this.permission = "chorus.command.hud"
        commandParameters.clear()
        this.addCommandParameters(
            "default", arrayOf(
                CommandParameter.Companion.newType("player", false, CommandParamType.TARGET, PlayersNode()),
                CommandParameter.Companion.newEnum("visible", false, arrayOf("hide", "reset")),
                CommandParameter.Companion.newEnum(
                    "hud_element",
                    false,
                    arrayOf(
                        "armor",
                        "air_bubbles_bar",
                        "crosshair",
                        "food_bar",
                        "health",
                        "hotbar",
                        "paper_doll",
                        "tool_tips",
                        "progress_bar",
                        "touch_controls",
                        "vehicle_health"
                    )
                )
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

        val players = list!!.getResult<List<Player>>(0)!!
        if (players.isEmpty()) {
            log.addNoTargetMatch().output()
            return 0
        }

        val visibility = when (list.getResult<Any>(1) as String) {
            "hide" -> HudVisibility.HIDE
            "reset" -> HudVisibility.RESET
            else -> null
        }

        val element = when (list.getResult<Any>(2) as String) {
            "armor" -> HudElement.ARMOR
            "air_bubbles_bar" -> HudElement.AIR_BUBBLES_BAR
            "crosshair" -> HudElement.CROSSHAIR
            "food_bar" -> HudElement.FOOD_BAR
            "health" -> HudElement.HEALTH
            "hotbar" -> HudElement.HOTBAR
            "paper_doll" -> HudElement.PAPER_DOLL
            "tool_tips" -> HudElement.TOOL_TIPS
            "progress_bar" -> HudElement.PROGRESS_BAR
            "touch_controls" -> HudElement.TOUCH_CONTROLS
            "vehicle_health" -> HudElement.VEHICLE_HEALTH
            else -> null
        }

        if (visibility == null || element == null) {
            return 0
        }


        for (player in players) {
            val packet = SetHudPacket()
            packet.elements.add(element)
            packet.visibility = visibility
            player.dataPacket(packet)

            return 1
        }

        return 0
    }
}
