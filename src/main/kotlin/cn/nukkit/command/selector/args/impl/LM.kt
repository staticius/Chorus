package cn.nukkit.command.selector.args.impl

import cn.nukkit.Player
import cn.nukkit.command.CommandSender
import cn.nukkit.command.exceptions.SelectorSyntaxException
import cn.nukkit.entity.Entity
import cn.nukkit.level.Transform
import java.util.function.Predicate

class LM : CachedSimpleSelectorArgument() {
    @Throws(SelectorSyntaxException::class)
    override fun cache(
        selectorType: SelectorType?,
        sender: CommandSender?,
        basePos: Transform?,
        vararg arguments: String
    ): Predicate<Entity> {
        ParseUtils.singleArgument(arguments, keyName)
        ParseUtils.cannotReversed(arguments[0])
        val lm = arguments[0].toInt()
        return Predicate { entity: Entity -> entity is Player && entity.experienceLevel >= lm }
    }

    val keyName: String
        get() = "lm"

    val priority: Int
        get() = 3
}
