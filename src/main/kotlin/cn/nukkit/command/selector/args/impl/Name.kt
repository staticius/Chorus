package cn.nukkit.command.selector.args.impl

import cn.nukkit.command.CommandSender
import cn.nukkit.entity.Entity
import cn.nukkit.level.Transform
import java.util.function.Predicate

class Name : CachedSimpleSelectorArgument() {
    override fun cache(
        selectorType: SelectorType?,
        sender: CommandSender?,
        basePos: Transform?,
        vararg arguments: String
    ): Predicate<Entity> {
        val have = ArrayList<String>()
        val dontHave = ArrayList<String>()
        for (name in arguments) {
            var name = name
            val reversed: Boolean = ParseUtils.checkReversed(name)
            if (reversed) {
                name = name.substring(1)
                dontHave.add(name)
            } else have.add(name)
        }
        return Predicate { entity: Entity ->
            have.stream().allMatch { name: String -> entity.name == name } && dontHave.stream()
                .noneMatch { name: String -> entity.name == name }
        }
    }

    val keyName: String
        get() = "name"

    val priority: Int
        get() = 4
}
