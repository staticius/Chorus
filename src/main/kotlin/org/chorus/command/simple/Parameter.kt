package org.chorus.command.simple

import org.chorus.command.data.CommandParamType

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(
    AnnotationRetention.RUNTIME
)
annotation class Parameter(
    val name: String,
    val type: CommandParamType = CommandParamType.RAWTEXT,
    val optional: Boolean = false
)
