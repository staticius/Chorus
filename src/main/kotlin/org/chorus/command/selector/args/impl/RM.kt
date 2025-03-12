package org.chorus.command.selector.args.impl

import org.chorus.command.CommandSender
import org.chorus.command.exceptions.SelectorSyntaxException
import org.chorus.entity.Entity
import org.chorus.level.Transform
import java.util.function.Predicate

class RM : ISelectorArgument {
    @Throws(SelectorSyntaxException::class)
    override fun getPredicate(
        selectorType: SelectorType?,
        sender: CommandSender?,
        basePos: Transform,
        vararg arguments: String
    ): Predicate<Entity> {
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
