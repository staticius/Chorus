package cn.nukkit.command.simple

import cn.nukkit.command.data.CommandParamType

/**
 * @author nilsbrychzy
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(
    AnnotationRetention.RUNTIME
)
annotation class Parameter(
    val name: String,
    val type: CommandParamType = CommandParamType.RAWTEXT,
    val optional: Boolean = false
)
