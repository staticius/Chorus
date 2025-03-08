package cn.nukkit.command.simple

/**
 * @author nilsbrychzy
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(
    AnnotationRetention.RUNTIME
)
annotation class CommandParameters(val parameters: Array<Parameters> = [])
