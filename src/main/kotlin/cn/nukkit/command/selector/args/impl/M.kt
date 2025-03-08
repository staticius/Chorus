package cn.nukkit.command.selector.args.impl

import cn.nukkit.Player
import cn.nukkit.command.CommandSender
import cn.nukkit.command.exceptions.SelectorSyntaxException
import cn.nukkit.entity.Entity
import cn.nukkit.level.Transform
import java.util.function.Predicate

class M : CachedSimpleSelectorArgument() {
    @Throws(SelectorSyntaxException::class)
    override fun cache(
        selectorType: SelectorType?,
        sender: CommandSender?,
        basePos: Transform?,
        vararg arguments: String
    ): Predicate<Entity> {
        ParseUtils.singleArgument(arguments, keyName)
        var gmStr = arguments[0]
        val reversed: Boolean = ParseUtils.checkReversed(gmStr)
        if (reversed) gmStr = gmStr.substring(1)
        val gm: Int = ParseUtils.parseGameMode(gmStr)
        return Predicate { entity: Entity -> entity is Player && (reversed != (entity.gamemode == gm)) }
    }

    val keyName: String
        get() = "m"

    val priority: Int
        get() = 3
}
