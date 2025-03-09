package org.chorus.command.selector.args.impl

import org.chorus.command.CommandSender
import org.chorus.command.exceptions.SelectorSyntaxException
import org.chorus.entity.Entity
import org.chorus.level.Transform
import java.util.function.Predicate

class R : ISelectorArgument {
    @Throws(SelectorSyntaxException::class)
    override fun getPredicate(
        selectorType: SelectorType?,
        sender: CommandSender?,
        basePos: Transform,
        vararg arguments: String
    ): Predicate<Entity>? {
        ParseUtils.singleArgument(arguments, keyName)
        ParseUtils.cannotReversed(arguments[0])
        val r = arguments[0].toDouble()
        return Predicate<Entity> { entity: Entity -> entity.position.distanceSquared(basePos.position) < r.pow(2.0) }
    }

    val keyName: String
        get() = "r"

    val priority: Int
        get() = 3
}
