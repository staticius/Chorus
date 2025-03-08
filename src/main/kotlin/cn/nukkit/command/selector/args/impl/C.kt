package cn.nukkit.command.selector.args.impl

import cn.nukkit.command.CommandSender
import cn.nukkit.command.exceptions.SelectorSyntaxException
import cn.nukkit.entity.Entity
import cn.nukkit.level.Transform
import java.util.*
import java.util.function.Function
import kotlin.math.abs

class C : CachedFilterSelectorArgument() {
    @Throws(SelectorSyntaxException::class)
    override fun cache(
        selectorType: SelectorType?,
        sender: CommandSender?,
        basePos: Transform,
        vararg arguments: String
    ): Function<List<Entity?>, List<Entity?>> {
        ParseUtils.singleArgument(arguments, keyName)
        ParseUtils.cannotReversed(arguments[0])
        val c = arguments[0].toInt()
        if (c == 0) throw SelectorSyntaxException("C cannot be zero!")
        return Function { entities: List<Entity?> ->
            entities.sort(Comparator.comparingDouble { e: Entity ->
                e.position.distanceSquared(
                    basePos.position
                )
            })
            if (c < 0) Collections.reverse(entities)
            entities.subList(0, abs(c.toDouble()).toInt())
        }
    }

    val keyName: String
        get() = "c"

    val priority: Int
        get() = 3
}
