package org.chorus.resourcepacks.loader

import cn.nukkit.Server
import cn.nukkit.resourcepacks.JarPluginResourcePack
import cn.nukkit.resourcepacks.ResourcePack
import com.google.common.io.Files
import lombok.extern.slf4j.Slf4j
import java.io.File
import java.util.*

@Slf4j
class JarPluginResourcePackLoader(protected val jarPath: File) : ResourcePackLoader {
    override fun loadPacks(): List<ResourcePack> {
        val baseLang = Server.getInstance().language
        val loadedResourcePacks: MutableList<ResourcePack> = ArrayList()
        for (jar in Objects.requireNonNull<Array<File>>(jarPath.listFiles())) {
            try {
                var resourcePack: ResourcePack? = null
                val fileExt = Files.getFileExtension(jar.name)
                if (!jar.isDirectory) {
                    if (fileExt == "jar" && JarPluginResourcePack.Companion.hasResourcePack(jar)) {
                        JarPluginResourcePackLoader.log.info(baseLang.tr("nukkit.resources.plugin.loading", jar.name))
                        resourcePack = JarPluginResourcePack(jar)
                    }
                }
                if (resourcePack != null) {
                    loadedResourcePacks.add(resourcePack)
                    JarPluginResourcePackLoader.log.info(
                        baseLang.tr(
                            "nukkit.resources.plugin.loaded",
                            jar.name,
                            resourcePack.packName
                        )
                    )
                }
            } catch (e: IllegalArgumentException) {
                JarPluginResourcePackLoader.log.warn(baseLang.tr("nukkit.resources.fail", jar.name, e.message!!), e)
            }
        }
        return loadedResourcePacks
    }
}
