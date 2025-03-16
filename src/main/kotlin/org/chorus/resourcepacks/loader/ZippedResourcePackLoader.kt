package org.chorus.resourcepacks.loader

import com.google.common.io.Files
import org.chorus.Server
import org.chorus.resourcepacks.ResourcePack
import org.chorus.resourcepacks.ZippedResourcePack
import org.chorus.utils.Loggable
import java.io.File


class ZippedResourcePackLoader(//资源包文件存放地址
    protected val path: File
) : ResourcePackLoader {
    init {
        if (!path.exists()) {
            path.mkdirs()
        } else require(path.isDirectory) {
            Server.instance.baseLang
                .tr("nukkit.resources.invalid-path", path.name)
        }
    }

    override fun loadPacks(): List<ResourcePack> {
        val baseLang = Server.instance.baseLang
        val loadedResourcePacks: MutableList<ResourcePack> = ArrayList()
        for (pack in path.listFiles()!!) {
            try {
                var resourcePack: ResourcePack? = null
                val fileExt = Files.getFileExtension(pack.name)
                if (!pack.isDirectory && fileExt != "key") { //directory resource packs temporarily unsupported
                    when (fileExt) {
                        "zip", "mcpack" -> resourcePack = ZippedResourcePack(pack)
                        else -> ZippedResourcePackLoader.log.warn(
                            baseLang.tr(
                                "nukkit.resources.unknown-format",
                                pack.name
                            )
                        )
                    }
                }
                if (resourcePack != null) {
                    loadedResourcePacks.add(resourcePack)
                    ZippedResourcePackLoader.log.info(baseLang.tr("nukkit.resources.zip.loaded", pack.name))
                }
            } catch (e: IllegalArgumentException) {
                ZippedResourcePackLoader.log.warn(baseLang.tr("nukkit.resources.fail", pack.name, e.message!!), e)
            }
        }
        return loadedResourcePacks
    }

    companion object : Loggable
}
