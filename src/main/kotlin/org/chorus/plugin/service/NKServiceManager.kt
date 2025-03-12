package org.chorus.plugin.service

import com.google.common.base.Preconditions
import com.google.common.collect.ImmutableList
import org.chorus.Server
import org.chorus.plugin.*
import java.util.*


/**
 * @since 16-11-20
 */
class NKServiceManager : ServiceManager {
    private val handle: MutableMap<Class<*>, MutableList<RegisteredServiceProvider<*>>> = HashMap()

    override fun <T> register(service: Class<T>, provider: T, plugin: Plugin?, priority: ServicePriority): Boolean {
        Preconditions.checkNotNull<T>(provider)
        Preconditions.checkNotNull(priority)
        Preconditions.checkNotNull(service)

        // build-in service provider needn't plugin param
        if (plugin == null && provider.javaClass.getClassLoader() !== Server::class.java.classLoader) {
            throw NullPointerException("plugin")
        }

        return provide(service, provider, plugin, priority)
    }

    protected fun <T> provide(service: Class<T>, instance: T, plugin: Plugin?, priority: ServicePriority): Boolean {
        synchronized(handle) {
            val list: List<RegisteredServiceProvider<*>> =
                handle.computeIfAbsent(service) { k: Class<*>? -> ArrayList() }
            val registered = RegisteredServiceProvider(service, instance, priority, plugin)

            val position = Collections.binarySearch(list, registered)

            if (position > -1) return false
            list.add(-(position + 1), registered)
        }

        return true
    }

    override fun cancel(plugin: Plugin): List<RegisteredServiceProvider<*>> {
        val builder = ImmutableList.builder<RegisteredServiceProvider<*>>()

        var it: MutableIterator<RegisteredServiceProvider<*>>
        var registered: RegisteredServiceProvider<*>

        synchronized(handle) {
            for (list in handle.values) {
                it = list.iterator()

                while (it.hasNext()) {
                    registered = it.next()
                    if (registered.plugin === plugin) {
                        it.remove()
                        builder.add(registered)
                    }
                }
            }
        }

        return builder.build()
    }

    override fun <T> cancel(service: Class<T>, provider: T): RegisteredServiceProvider<T>? {
        var result: RegisteredServiceProvider<T>? = null

        synchronized(handle) {
            val it = handle[service]!!.iterator()
            var next: RegisteredServiceProvider<*>
            while (it.hasNext() && result == null) {
                next = it.next()
                if (next.provider === provider) {
                    it.remove()
                    result = next
                }
            }
        }

        return result
    }

    override fun <T> getProvider(service: Class<T>): RegisteredServiceProvider<T>? {
        synchronized(handle) {
            val list: List<RegisteredServiceProvider<*>>? = handle[service]
            if (list == null || list.isEmpty()) return null
            return list[0] as RegisteredServiceProvider<T>
        }
    }

    override val knownService: List<Class<*>?>?
        get() = ImmutableList.copyOf(handle.keys)

    override fun getRegistrations(plugin: Plugin): List<RegisteredServiceProvider<*>> {
        val builder = ImmutableList.builder<RegisteredServiceProvider<*>>()
        synchronized(handle) {
            for (registered in handle.values) {
                for (provider in registered) {
                    if (provider.plugin == plugin) {
                        builder.add(provider)
                    }
                }
            }
        }
        return builder.build()
    }

    override fun <T> getRegistrations(service: Class<T>): List<RegisteredServiceProvider<T>> {
        val builder = ImmutableList.builder<RegisteredServiceProvider<T>>()
        synchronized(handle) {
            val registered: List<RegisteredServiceProvider<*>>? = handle[service]
            if (registered == null) {
                val empty = ImmutableList.of<RegisteredServiceProvider<T>>()
                return empty
            }
            for (provider in registered) {
                builder.add(provider as RegisteredServiceProvider<T>)
            }
        }
        return builder.build()
    }

    override fun <T> isProvidedFor(service: Class<T>): Boolean {
        synchronized(handle) {
            return handle.containsKey(service)
        }
    }
}
