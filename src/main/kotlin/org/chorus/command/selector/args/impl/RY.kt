package org.chorus.command.selector.args.impl

import cn.nukkit.command.CommandSender
import cn.nukkit.command.exceptions.SelectorSyntaxException
import cn.nukkit.entity.Entity
import cn.nukkit.level.Transform
import java.util.function.Predicate

class RY : CachedSimpleSelectorArgument() {
    @Throws(SelectorSyntaxException::class)
    override fun cache(
        selectorType: SelectorType?,
        sender: CommandSender?,
        basePos: Transform?,
        vararg arguments: String
    ): Predicate<Entity> {
        ParseUtils.singleArgument(arguments, keyName)
        ParseUtils.cannotReversed(arguments[0])
        val ry = arguments[0].toDouble()
        if (!ParseUtils.checkBetween(
                -180.0,
                180.0,
                ry
            )
        ) throw SelectorSyntaxException("RX out of bound (-180 - 180): $ry")
        //获取到的yaw范围是[0, 360]，而原版规定的范围是[-180, 180]。故减去一个180
        //并还要转换到原版的坐标系(+z为正南 etc...)
        return Predicate { entity: Entity -> ((entity.rotation.yaw + 90) % 360 - 180) <= ry }
    }

    val keyName: String
        get() = "ry"

    val priority: Int
        get() = 3
}
