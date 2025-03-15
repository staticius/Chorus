package org.chorus.command.selector.args.impl

import org.chorus.command.CommandSender
import org.chorus.command.exceptions.SelectorSyntaxException
import org.chorus.command.selector.ParseUtils
import org.chorus.command.selector.SelectorType
import org.chorus.command.selector.args.ISelectorArgument
import org.chorus.entity.Entity
import org.chorus.level.Transform
import java.util.function.Predicate
import kotlin.math.pow

class RM : ISelectorArgument {
    @Throws(SelectorSyntaxException::class)
    override fun getPredicate(
        selectorType: SelectorType?,
        sender: CommandSender?,
        basePos: Transform,
        vararg arguments: String
    ): Predicate<Entity> {
        ParseUtils.singleArgument(arguments.toList().toTypedArray(), keyName)
        ParseUtils.cannotReversed(arguments[0])
        val rm = arguments[0].toDouble()
        return Predicate { entity: Entity -> entity.position.distanceSquared(basePos.position) > rm.pow(2.0) }
    }

    override val keyName: String
        get() = "rm"

    override val priority: Int
        get() = 3
}
