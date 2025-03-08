package cn.nukkit.api

/**
 * Describe the deprecation with more details. This is persisted to the class file, so it can be read without javadocs.
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
annotation class DeprecationDetails(
    /**
     * The version which marked this element as deprecated.
     */
    val since: String,
    /**
     * Why it is deprecated.
     */
    val reason: String,
    /**
     * What should be used or do instead.
     */
    val replaceWith: String = "",
    /**
     * The maintainer party that has added this depreciation. For example: PowerNukkit, Cloudburst Nukkit, and Nukkit
     */
    val by: String = ""
)
