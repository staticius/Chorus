package org.chorus.api

/**
 * DoNotModify is used to indicate that the return value of method, variables, etc. should not be modified
 *
 *
 * DoNotModify注解用于标明方法的返回值，变量等不应该被修改
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
annotation class DoNotModify 
