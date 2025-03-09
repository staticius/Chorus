package org.chorus.utils.collection

/**
 * 声明此操作将会解冻可冻结数组
 */
@Retention(AnnotationRetention.SOURCE)
@Target(
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@MustBeDocumented
annotation class ShouldThaw 
