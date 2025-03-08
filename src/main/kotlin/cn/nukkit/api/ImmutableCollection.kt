package cn.nukkit.api

/**
 * ImmutableCollection is used to mark a collection as immutable.
 *
 *
 * ImmutableCollection是用来标记一个集合为不可变的。
 */
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER
)
annotation class ImmutableCollection 
