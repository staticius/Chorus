package org.chorus.entity.data

import java.util.function.Function

open class EntityDataType<out T : Any> {
    private val name: String
    private val type: Class<out T>
    private val value: Int
    private val transformer: Function<out T, *>
    private val defaultValue: T

    constructor(defaultValue: T, name: String, value: Int) {
        this.name = name
        this.type = defaultValue::class.java
        this.defaultValue = defaultValue
        this.value = value
        this.transformer = Function.identity()
    }

    constructor(defaultValue: T, name: String, value: Int, transformer: Function<T, *>) {
        this.name = name
        this.type = defaultValue::class.java
        this.defaultValue = defaultValue
        this.value = value
        this.transformer = transformer
    }

    open fun isInstance(value: Any?): Boolean {
        return type.isInstance(value)
    }

    fun getTypeName(): String {
        return type.getTypeName()
    }

    fun getType(): Class<out T> {
        return type
    }

    fun getDefaultValue(): T {
        return defaultValue
    }

    fun getTransformer(): Function<out T, *> {
        return transformer
    }

    fun getValue(): Int {
        return value
    }

    override fun toString(): String {
        return name
    }
}
