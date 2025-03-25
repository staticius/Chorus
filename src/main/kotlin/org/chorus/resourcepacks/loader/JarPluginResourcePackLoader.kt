package org.chorus.resourcepacks.loader

import com.google.common.io.Files
import org.chorus.Server
import org.chorus.resourcepacks.JarPluginResourcePack
import org.chorus.resourcepacks.ResourcePack
import org.chorus.utils.Loggable
import java.io.File
import java.util.*


class JarPluginResourcePackLoader(protected val jarPath: File) : ResourcePackLoader {
    override fun loadPacks(): List<ResourcePack> {
        val baseLang = Server.instance.baseLang
        val loadedResourcePacks: MutableList<ResourcePack> = ArrayList()
        for (jar in Objects.requireNonNull<Array<File>>(jarPath.listFiles())) {
            try {
                var resourcePack: ResourcePack? = null
                val fileExt = Files.getFileExtension(jar.name)
                if (!jar.isDirectory) {
                    if (fileExt == "jar" && JarPluginResourcePack.hasResourcePack(jar)) {
                        JarPluginResourcePackLoader.log.info(baseLang.tr("chorus.resources.plugin.loading", jar.name))
                        resourcePack = JarPluginResourcePack(jar)
                    }
                }
                if (resourcePack != null) {
                    loadedResourcePacks.add(resourcePack)
                    JarPluginResourcePackLoader.log.info(
                        baseLang.tr(
                            "chorus.resources.plugin.loaded",
                            jar.name,
                            resourcePack.packName ?: ""
                        )
                    )
                }
            } catch (e: IllegalArgumentException) {
                JarPluginResourcePackLoader.log.warn(baseLang.tr("chorus.resources.fail", jar.name, e.message!!), e)
            }
        }
        return loadedResourcePacks
    }

    companion object : Loggable
}
