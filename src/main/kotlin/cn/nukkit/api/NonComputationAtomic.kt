package cn.nukkit.api

/**
 * Marks that the annotated element is not computation atomic, and its computeXXX methods may not be atomic.
 */
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS,
    AnnotationTarget.FIELD,
    AnnotationTarget.FILE
)
@MustBeDocumented
annotation class NonComputationAtomic 
