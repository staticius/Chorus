package org.chorus.resourcepacks.loader

import org.chorus.Server
import org.chorus.resourcepacks.JarPluginResourcePack
import org.chorus.resourcepacks.ResourcePack
import com.google.common.io.Files

import java.io.File
import java.util.*


class JarPluginResourcePackLoader(protected val jarPath: File) : ResourcePackLoader {
    override fun loadPacks(): List<ResourcePack> {
        val baseLang = Server.instance.language
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
