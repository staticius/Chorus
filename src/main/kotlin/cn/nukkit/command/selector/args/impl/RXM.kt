package cn.nukkit.command.selector.args.impl

import cn.nukkit.command.CommandSender
import cn.nukkit.command.exceptions.SelectorSyntaxException
import cn.nukkit.entity.Entity
import cn.nukkit.level.Transform
import java.util.function.Predicate

class RXM : CachedSimpleSelectorArgument() {
    @Throws(SelectorSyntaxException::class)
    override fun cache(
        selectorType: SelectorType?,
        sender: CommandSender?,
        basePos: Transform?,
        vararg arguments: String
    ): Predicate<Entity> {
        ParseUtils.singleArgument(arguments, keyName)
        ParseUtils.cannotReversed(arguments[0])
        val rxm = arguments[0].toDouble()
        if (!ParseUtils.checkBetween(
                -90.0,
                90.0,
                rxm
            )
        ) throw SelectorSyntaxException("RXM out of bound (-90 - 90): $rxm")
        return Predicate { entity: Entity -> entity.rotation.pitch >= rxm }
    }

    val keyName: String
        get() = "rxm"

    val priority: Int
        get() = 3
}
