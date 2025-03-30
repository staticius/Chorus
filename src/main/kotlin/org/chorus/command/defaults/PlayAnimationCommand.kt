package org.chorus.command.defaults

import org.chorus.command.CommandSender
import org.chorus.command.data.CommandParamType
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.utils.CommandLogger
import org.chorus.entity.Entity
import org.chorus.network.protocol.AnimateEntityPacket
import kotlin.collections.set

class PlayAnimationCommand(name: String) : VanillaCommand(name, "commands.playanimation.description") {
    init {
        this.permission = "chorus.command.playanimation"
        commandParameters.clear()
        commandParameters["default"] = arrayOf(
            CommandParameter.Companion.newType("entity", CommandParamType.TARGET),
            CommandParameter.Companion.newType("animation", CommandParamType.STRING),
            CommandParameter.Companion.newType("next_state", true, CommandParamType.STRING),
            CommandParameter.Companion.newType("blend_out_time", true, CommandParamType.FLOAT),
            CommandParameter.Companion.newType("stop_expression", true, CommandParamType.STRING),
            CommandParameter.Companion.newType("controller", true, CommandParamType.STRING),
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
        val entities = list.getResult<List<Entity>>(0)!!
        if (entities.isEmpty()) {
            log.addNoTargetMatch().output()
            return 0
        }
        val animationBuilder = AnimateEntityPacket.Animation(
            animation = list.getResult(1)!!,
            nextState = when (list.hasResult(2)) {
                true -> list.getResult(2)!!
                else -> AnimateEntityPacket.Animation.DEFAULT_NEXT_STATE
            },
            blendOutTime = when (list.hasResult(2)) {
                true -> list.getResult(2)!!
                else -> AnimateEntityPacket.Animation.DEFAULT_BLEND_OUT_TIME
            },
            stopExpression = when (list.hasResult(2)) {
                true -> list.getResult(2)!!
                else -> AnimateEntityPacket.Animation.DEFAULT_STOP_EXPRESSION
            },
            controller = when (list.hasResult(2)) {
                true -> list.getResult(2)!!
                else -> AnimateEntityPacket.Animation.DEFAULT_CONTROLLER
            },
            stopExpressionVersion = AnimateEntityPacket.Animation.DEFAULT_STOP_EXPRESSION_VERSION
        )
        // send animation
        Entity.playAnimationOnEntities(animationBuilder, entities)
        log.addSuccess("commands.playanimation.success").output()
        return 1
    }
}
