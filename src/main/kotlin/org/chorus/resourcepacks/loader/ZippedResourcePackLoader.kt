package org.chorus.resourcepacks.loader

import cn.nukkit.Server
import cn.nukkit.resourcepacks.ResourcePack
import cn.nukkit.resourcepacks.ZippedResourcePack
import com.google.common.io.Files
import lombok.extern.slf4j.Slf4j
import java.io.File

@Slf4j
class ZippedResourcePackLoader(//资源包文件存放地址
    protected val path: File
) : ResourcePackLoader {
    init {
        if (!path.exists()) {
            path.mkdirs()
        } else require(path.isDirectory) {
            Server.getInstance().language
                .tr("nukkit.resources.invalid-path", path.name)
        }
    }

    override fun loadPacks(): List<ResourcePack> {
        val baseLang = Server.getInstance().language
        val loadedResourcePacks: MutableList<ResourcePack> = ArrayList()
        for (pack in path.listFiles()) {
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
}
