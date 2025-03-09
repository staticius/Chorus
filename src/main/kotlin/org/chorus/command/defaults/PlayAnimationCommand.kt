package org.chorus.command.defaults

import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.utils.CommandLogger
import cn.nukkit.entity.Entity
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.set

class PlayAnimationCommand(name: String) : VanillaCommand(name, "commands.playanimation.description") {
    init {
        this.permission = "nukkit.command.playanimation"
        commandParameters.clear()
        commandParameters["default"] = arrayOf<CommandParameter?>(
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
        result: Map.Entry<String, ParamList?>,
        log: CommandLogger
    ): Int {
        val list = result.value
        val entities = list!!.getResult<List<Entity>>(0)!!
        if (entities.isEmpty()) {
            log.addNoTargetMatch().output()
            return 0
        }
        val animationBuilder: Animation.AnimationBuilder = AnimateEntityPacket.Animation.builder()
        val animation = list.getResult<String>(1)
        animationBuilder.animation(animation)
        //optional
        if (list.hasResult(2)) {
            val next_state = list.getResult<String>(2)
            animationBuilder.nextState(next_state)
        }
        if (list.hasResult(3)) {
            val blend_out_time = list.getResult<Float>(3)!!
            animationBuilder.blendOutTime(blend_out_time)
        }
        if (list.hasResult(4)) {
            val stop_expression = list.getResult<String>(4)
            animationBuilder.stopExpression(stop_expression)
        }
        if (list.hasResult(5)) {
            val controller = list.getResult<String>(5)
            animationBuilder.controller(controller)
        }
        //send animation
        Entity.playAnimationOnEntities(animationBuilder.build(), entities)
        log.addSuccess("commands.playanimation.success").output()
        return 1
    }
}
