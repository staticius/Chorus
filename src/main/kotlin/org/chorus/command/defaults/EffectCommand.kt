package org.chorus.command.defaults

import org.chorus.command.*
import org.chorus.command.data.CommandEnum
import org.chorus.command.data.CommandParamType
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.utils.CommandLogger
import org.chorus.entity.Entity
import org.chorus.entity.effect.Effect
import kotlin.collections.set

/**
 * @author Snake1999 and Pub4Game
 * @since 2016/1/23
 */
class EffectCommand(name: String) : Command(name, "commands.effect.description", "nukkit.command.effect.usage") {
    init {
        this.permission = "nukkit.command.effect"
        commandParameters.clear()
        commandParameters["default"] = arrayOf(
            CommandParameter.Companion.newType("player", CommandParamType.TARGET),
            CommandParameter.Companion.newEnum("effect", CommandEnum.Companion.ENUM_EFFECT),
            CommandParameter.Companion.newType("seconds", true, CommandParamType.INT),
            CommandParameter.Companion.newType("amplifier", true, CommandParamType.INT),
            CommandParameter.Companion.newEnum("hideParticle", true, CommandEnum.Companion.ENUM_BOOLEAN)
        )
        commandParameters["clear"] = arrayOf(
            CommandParameter.Companion.newType("player", CommandParamType.TARGET),
            CommandParameter.Companion.newEnum("clear", CommandEnum("ClearEffects", "clear"))
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
        var entities = list!!.getResult<List<Entity>>(0)!!
        entities = entities.stream().filter { e: Entity? -> e !is EntityItem }.toList()
        if (entities.isEmpty()) {
            log.addNoTargetMatch().output()
            return 0
        }
        when (result.key) {
            "default" -> {
                val effect: Effect
                val str = list.getResult<String>(1)
                try {
                    effect = Effect.get(str)
                } catch (e: RuntimeException) {
                    log.addError("commands.effect.notFound", str).output()
                    return 0
                }
                var duration = 300
                var amplification = 0
                if (list.hasResult(2)) {
                    duration = list.getResult(2)!!
                    if (effect !is InstantEffect) {
                        duration *= 20
                    }
                } else if (effect is InstantEffect) {
                    duration = 1
                }
                if (duration < 0) {
                    log.addNumTooSmall(2, 0).output()
                    return 0
                }

                if (list.hasResult(3)) {
                    amplification = list.getResult(3)!!
                }
                if (amplification < 0) {
                    log.addNumTooSmall(3, 0).output()
                    return 0
                }

                if (list.hasResult(4)) {
                    val v = list.getResult<Boolean>(4)!!
                    effect.setVisible(!v)
                }

                var success = 0
                for (entity in entities) {
                    if (duration == 0) {
                        if (!entity.hasEffect(effect.type)) {
                            log.addError("commands.effect.failure.notActive", effect.name, entity.name).output()
                            continue
                        }
                        entity.removeEffect(effect.type)
                        log.addSuccess("commands.effect.success.removed", effect.name, entity.name).output()
                    } else {
                        effect.setDuration(duration).setAmplifier(amplification)
                        entity.addEffect(effect.clone())
                        log.addSuccess(
                            "%commands.effect.success",
                            effect.name,
                            effect.amplifier.toString(),
                            entity.name,
                            (effect.duration / 20).toString()
                        )
                            .output(true)
                    }
                    success++
                }
                return success
            }

            "clear" -> {
                var success = 0
                for (entity in entities) {
                    if (entity.effects.isEmpty()) {
                        log.addError("commands.effect.failure.notActive.all", entity.name)
                        continue
                    }
                    for (effect in entity.effects.values) {
                        entity.removeEffect(effect.type)
                    }
                    success++
                    log.addSuccess("commands.effect.success.removed.all", entity.name)
                }
                log.successCount(success).output()
                return success
            }

            else -> {
                return 0
            }
        }
    }
}
