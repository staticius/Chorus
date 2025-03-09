package org.chorus.command.selector.args.impl

import cn.nukkit.command.CommandSender
import cn.nukkit.command.exceptions.SelectorSyntaxException
import cn.nukkit.entity.Entity
import cn.nukkit.level.Transform
import java.util.function.Predicate

class DZ : ScopeArgument() {
    @Throws(SelectorSyntaxException::class)
    override fun getPredicate(
        selectorType: SelectorType?,
        sender: CommandSender?,
        basePos: Transform,
        vararg arguments: String
    ): Predicate<Entity>? {
        ParseUtils.singleArgument(arguments, keyName)
        ParseUtils.cannotReversed(arguments[0])
        val z = basePos.z
        val dz = arguments[0].toDouble()
        return Predicate? { entity: Entity -> ParseUtils.checkBetween(z, z + dz, entity.z) }
    }

    val keyName: String
        get() = "dz"
}
