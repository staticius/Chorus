package org.chorus.command.defaults

import cn.nukkit.Player
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandEnum
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.utils.CommandLogger
import cn.nukkit.level.Locator
import kotlin.collections.ArrayList
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.set

/**
 * @author xtypr
 * @since 2015/11/12
 */
class ParticleCommand(name: String) : VanillaCommand(name, "commands.particle.description") {
    init {
        this.permission = "nukkit.command.particle"
        commandParameters.clear()
        val particles: MutableList<String?> = ArrayList()
        for (particle in ParticleEffect.entries) {
            particles.add(particle.getIdentifier())
        }
        commandParameters["default"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum("effect", CommandEnum("particle", particles, true)),
            CommandParameter.Companion.newType("position", CommandParamType.POSITION),
            CommandParameter.Companion.newType("count", true, CommandParamType.INT)
        )
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList?>,
        log: CommandLogger
    ): Int {
        val name = result.value!!.getResult<String>(0)
        val locator = result.value!!.getResult<Locator>(1)
        var count = 1
        if (result.value!!.hasResult(2)) count = result.value!!.getResult(2)!!
        if (count < 1) {
            log.addNumTooSmall(2, 1).output()
            return 0
        }
        for (i in 0..<count) {
            locator!!.level.addParticleEffect(
                locator.position.asVector3f(),
                name,
                -1,
                locator.level.dimension,
                *null as Array<Player?>?
            )
        }
        log.addSuccess("commands.particle.success", name, count.toString())
        return 1
    }
}
