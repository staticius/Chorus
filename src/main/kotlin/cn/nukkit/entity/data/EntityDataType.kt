package cn.nukkit.entity.data

import java.util.function.Function

open class EntityDataType<T> {
    private val name: String
    private val type: Class<*>
    private val value: Int
    private val transformer: Function<T?, *>
    private val defaultValue: T

    constructor(type: T, name: String, value: Int) {
        this.name = name
        this.type = type.javaClass
        this.defaultValue = type
        this.value = value
        this.transformer = Function.identity()
    }

    constructor(type: T, name: String, value: Int, transformer: Function<T?, *>) {
        this.name = name
        this.type = type.javaClass
        this.defaultValue = type
        this.value = value
        this.transformer = transformer
    }

    open fun isInstance(value: Any?): Boolean {
        return type.isInstance(value)
    }

    fun getTypeName(): String {
        return type.getTypeName()
    }

    fun getType(): Class<*> {
        return type
    }

    fun getDefaultValue(): T {
        return defaultValue
    }

    fun getTransformer(): Function<Any?, Any> {
        return transformer as Function<Any?, Any>
    }

    fun getValue(): Int {
        return value
    }

    override fun toString(): String {
        return name
    }
}
