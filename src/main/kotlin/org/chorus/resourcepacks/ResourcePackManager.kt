package org.chorus.resourcepacks

import org.chorus.Server
import org.chorus.resourcepacks.loader.ResourcePackLoader
import org.chorus.resourcepacks.loader.ZippedResourcePackLoader
import com.google.common.collect.Sets
import lombok.extern.slf4j.Slf4j
import java.io.File
import java.util.*
import java.util.function.Consumer


class ResourcePackManager(private val loaders: MutableSet<ResourcePackLoader>) {
    var maxChunkSize: Int = 1024 * 32 // 32kb is default

    private val resourcePacksById: MutableMap<UUID?, ResourcePack?> = HashMap()
    private val resourcePacks: MutableSet<ResourcePack?> = HashSet()


    init {
        reloadPacks()
    }

    constructor(vararg loaders: ResourcePackLoader?) : this(Sets.newHashSet<ResourcePackLoader>(*loaders))

    /**
     * 保留此方法仅仅为了向后兼容性以及测试
     *
     *
     * 请不要使用它
     */
    constructor(resourcePacksDir: File) : this(ZippedResourcePackLoader(resourcePacksDir))

    val resourceStack: Array<ResourcePack>
        get() = resourcePacks.toArray<ResourcePack>(ResourcePack.Companion.EMPTY_ARRAY)

    fun getPackById(id: UUID?): ResourcePack? {
        return resourcePacksById[id]
    }

    fun registerPackLoader(loader: ResourcePackLoader) {
        loaders.add(loader)
    }

    fun reloadPacks() {
        resourcePacksById.clear()
        resourcePacks.clear()
        loaders.forEach(Consumer { loader: ResourcePackLoader ->
            val loadedPacks = loader.loadPacks()
            loadedPacks!!.forEach(Consumer { pack: ResourcePack? ->
                resourcePacksById[pack.getPackId()] = pack
            })
            resourcePacks.addAll(loadedPacks)
        })

        ResourcePackManager.log.info(
            Server.getInstance().language
                .tr("nukkit.resources.success", resourcePacks.size.toString())
        )
    }
}
