package org.chorus.command.selector.args.impl

import org.chorus.command.CommandSender
import org.chorus.command.exceptions.SelectorSyntaxException
import org.chorus.command.selector.ParseUtils
import org.chorus.command.selector.SelectorType
import org.chorus.entity.Entity
import org.chorus.level.Transform
import java.util.function.Predicate

class DY : ScopeArgument() {
    @Throws(SelectorSyntaxException::class)
    override fun getPredicate(
        selectorType: SelectorType?,
        sender: CommandSender?,
        basePos: Transform,
        vararg arguments: String
    ): Predicate<Entity> {
        ParseUtils.singleArgument(arguments.toList().toTypedArray(), keyName)
        ParseUtils.cannotReversed(arguments[0])
        val y = basePos.y
        val dy = arguments[0].toDouble()
        return Predicate { entity: Entity -> ParseUtils.checkBetween(y, y + dy, entity.getY()) }
    }

    override val keyName: String
        get() = "dy"
}
