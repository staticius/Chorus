package cn.nukkit.command.selector.args.impl

import cn.nukkit.command.CommandSender
import cn.nukkit.command.exceptions.SelectorSyntaxException
import cn.nukkit.entity.Entity
import cn.nukkit.level.Transform
import java.util.function.Predicate

class RM : ISelectorArgument {
    @Throws(SelectorSyntaxException::class)
    override fun getPredicate(
        selectorType: SelectorType?,
        sender: CommandSender?,
        basePos: Transform,
        vararg arguments: String
    ): Predicate<Entity>? {
        ParseUtils.singleArgument(arguments, keyName)
        ParseUtils.cannotReversed(arguments[0])
        val rm = arguments[0].toDouble()
        return Predicate? { entity: Entity -> entity.position.distanceSquared(basePos.position) > rm.pow(2.0) }
    }

    val keyName: String
        get() = "rm"

    val priority: Int
        get() = 3
}
