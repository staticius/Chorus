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
        this.permission = "nukkit.command.playanimation"
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
        val animationBuilder = AnimateEntityPacket.Animation()
        val animation = list.getResult<String>(1)
        animationBuilder.animation = (animation)
        // optional
        if (list.hasResult(2)) {
            val nextState = list.getResult<String>(2)!!
            animationBuilder.nextState = (nextState)
        }
        if (list.hasResult(3)) {
            val blendOutTime = list.getResult<Float>(3)!!
            animationBuilder.blendOutTime = (blendOutTime)
        }
        if (list.hasResult(4)) {
            val stopExpression = list.getResult<String>(4)!!
            animationBuilder.stopExpression = (stopExpression)
        }
        if (list.hasResult(5)) {
            val controller = list.getResult<String>(5)!!
            animationBuilder.controller = (controller)
        }
        // send animation
        Entity.playAnimationOnEntities(animationBuilder, entities)
        log.addSuccess("commands.playanimation.success").output()
        return 1
    }
}
