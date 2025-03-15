package org.chorus.command.selector.args.impl

import org.chorus.Player
import org.chorus.command.CommandSender
import org.chorus.command.exceptions.SelectorSyntaxException
import org.chorus.command.selector.ParseUtils
import org.chorus.command.selector.SelectorType
import org.chorus.command.selector.args.CachedSimpleSelectorArgument
import org.chorus.entity.Entity
import org.chorus.level.Transform
import java.util.function.Predicate

class M : CachedSimpleSelectorArgument() {
    @Throws(SelectorSyntaxException::class)
    override fun cache(
        selectorType: SelectorType?,
        sender: CommandSender?,
        basePos: Transform?,
        vararg arguments: String
    ): Predicate<Entity> {
        ParseUtils.singleArgument(arguments.toList().toTypedArray(), keyName)
        var gmStr = arguments[0]
        val reversed: Boolean = ParseUtils.checkReversed(gmStr)
        if (reversed) gmStr = gmStr.substring(1)
        val gm: Int = ParseUtils.parseGameMode(gmStr)
        return Predicate { entity: Entity -> entity is Player && (reversed != (entity.gamemode == gm)) }
    }

    override val keyName: String
        get() = "m"

    override val priority: Int
        get() = 3
}
