package org.chorus.plugin.service

import org.chorus.plugin.Plugin


/**
 * @since 16-11-20
 */
class RegisteredServiceProvider<T> internal constructor(
    /**
     * Return the provided service.
     *
     * @return the provided service
     */
    val service: Class<T>,
    /**
     * Return the service provider.
     *
     * @return the service provider
     */
    val provider: T, val priority: ServicePriority,
    /**
     * Return the plugin provide this service.
     *
     * @return the plugin provide this service, or `null`
     * only if this service provided by server
     */
    val plugin: Plugin?
) :
    Comparable<RegisteredServiceProvider<T>> {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val that = o as RegisteredServiceProvider<*>

        return provider === that.provider || provider == that.provider
    }

    override fun hashCode(): Int {
        return provider.hashCode()
    }

    override fun compareTo(other: RegisteredServiceProvider<T>): Int {
        return other.priority.ordinal - priority.ordinal
    }
}
