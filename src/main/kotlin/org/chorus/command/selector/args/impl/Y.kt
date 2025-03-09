package org.chorus.command.selector.args.impl

import org.chorus.command.CommandSender
import org.chorus.command.exceptions.SelectorSyntaxException
import org.chorus.entity.Entity
import org.chorus.level.Transform
import java.util.function.Predicate

class Y : CoordinateArgument() {
    @Throws(SelectorSyntaxException::class)
    override fun getPredicate(
        selectorType: SelectorType?,
        sender: CommandSender?,
        basePos: Transform,
        vararg arguments: String
    ): Predicate<Entity>? {
        ParseUtils.singleArgument(arguments, keyName)
        ParseUtils.cannotReversed(arguments[0])
        basePos.setY(ParseUtils.parseOffsetDouble(arguments[0], basePos.y))
        return null
    }

    val keyName: String
        get() = "y"
}
