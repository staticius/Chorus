package org.chorus.command.selector.args.impl

import cn.nukkit.command.CommandSender
import cn.nukkit.entity.Entity
import cn.nukkit.level.Transform
import java.util.function.Predicate

class Tag : CachedSimpleSelectorArgument() {
    override fun cache(
        selectorType: SelectorType?,
        sender: CommandSender?,
        basePos: Transform?,
        vararg arguments: String
    ): Predicate<Entity> {
        val have = ArrayList<String>()
        val dontHave = ArrayList<String>()
        for (tag in arguments) {
            var tag = tag
            val reversed: Boolean = ParseUtils.checkReversed(tag)
            if (reversed) {
                tag = tag.substring(1)
                dontHave.add(tag)
            } else have.add(tag)
        }
        return Predicate { entity: Entity ->
            have.stream().allMatch { tag: String? -> entity.containTag(tag) } && dontHave.stream()
                .noneMatch { tag: String? -> entity.containTag(tag) }
        }
    }

    val keyName: String
        get() = "tag"

    val priority: Int
        get() = 4
}
