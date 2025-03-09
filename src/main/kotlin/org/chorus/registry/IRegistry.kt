package org.chorus.registry

import cn.nukkit.registry.RegisterException
import me.sunlan.fastreflection.FastMemberLoader
import org.jetbrains.annotations.ApiStatus

interface IRegistry<K, V, R> {
    fun init()

    fun get(key: K): V

    fun trim()

    fun reload()

    @Throws(RegisterException::class)
    fun register(key: K, value: R)

    companion object {
        @ApiStatus.Internal
        val fastMemberLoaderCache: HashMap<String, FastMemberLoader> = HashMap()
    }
}
