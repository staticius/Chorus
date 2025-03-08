package cn.nukkit.level.generator.terra

import cn.nukkit.Server
import cn.nukkit.block.*
import cn.nukkit.level.generator.terra.delegate.PNXBiomeDelegate
import cn.nukkit.level.generator.terra.handles.PNXItemHandle
import cn.nukkit.level.generator.terra.handles.PNXWorldHandle
import cn.nukkit.level.generator.terra.mappings.MappingRegistries
import cn.nukkit.plugin.InternalPlugin
import cn.nukkit.registry.RegisterException
import cn.nukkit.registry.Registries
import com.dfsek.tectonic.api.TypeRegistry
import com.dfsek.tectonic.api.depth.DepthTracker
import com.dfsek.tectonic.api.loader.ConfigLoader
import com.dfsek.terra.AbstractPlatform
import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent
import com.dfsek.terra.api.handle.ItemHandle
import com.dfsek.terra.api.handle.WorldHandle
import com.dfsek.terra.api.world.biome.PlatformBiome
import lombok.extern.slf4j.Slf4j
import java.io.*
import java.lang.reflect.AnnotatedType
import java.nio.file.Files
import java.nio.file.StandardCopyOption

@Slf4j
class PNXPlatform : AbstractPlatform() {
    override fun reload(): Boolean {
        terraConfig.load(this)
        rawConfigRegistry.clear()
        // TODO: 2022/2/14 支持重载配置
        return rawConfigRegistry.loadAll(this)
    }

    override fun platformName(): String {
        return "PowerNukkitX"
    }

    override fun runPossiblyUnsafeTask(task: Runnable) {
        Server.getInstance().scheduler.scheduleTask(InternalPlugin.INSTANCE, task)
    }

    override fun getWorldHandle(): WorldHandle {
        return pnxWorldHandle
    }

    override fun getDataFolder(): File {
        return DATA_PATH
    }

    override fun getItemHandle(): ItemHandle {
        return pnxItemHandle
    }

    override fun getVersion(): String {
        return super.getVersion()
    }

    override fun register(registry: TypeRegistry) {
        super.register(registry)
        registry.registerLoader(
            PlatformBiome::class.java
        ) { type: AnnotatedType?, o: Any, loader: ConfigLoader?, depthTracker: DepthTracker? ->
            parseBiome(
                o as String
            )
        }
            .registerLoader(
                BlockState::class.java
            ) { type: AnnotatedType?, o: Any, loader: ConfigLoader?, depthTracker: DepthTracker? ->
                pnxWorldHandle.createBlockState(
                    o as String
                )
            }
    }

    companion object {
        val DATA_PATH: File
        private val pnxWorldHandle = PNXWorldHandle()
        private val pnxItemHandle = PNXItemHandle()
        private var INSTANCE: PNXPlatform? = null

        init {
            try {
                Registries.GENERATOR.register("terra", TerraGenerator::class.java)
            } catch (e: RegisterException) {
                throw RuntimeException(e)
            }
            DATA_PATH = File("./terra")
            if (!DATA_PATH.exists()) {
                if (!DATA_PATH.mkdirs()) {
                    PNXPlatform.log.info("Failed to create terra config folder.")
                }
            }
            val targetFile = File("./terra/config.yml")
            if (!targetFile.exists()) {
                try {
                    val terraDefaultConfigStream =
                        Server::class.java.classLoader.getResourceAsStream("terra_default_config.yml")
                    if (terraDefaultConfigStream != null) {
                        Files.copy(terraDefaultConfigStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
                    } else {
                        PNXPlatform.log.info("Failed to extract terra config.")
                    }
                } catch (e: IOException) {
                    PNXPlatform.log.info("Failed to extract terra config.")
                }
            }
            val blocks = MappingRegistries.BLOCKS //load mapping
        }

        @JvmStatic
        @get:Synchronized
        val instance: PNXPlatform
            get() {
                if (INSTANCE != null) {
                    return INSTANCE!!
                }
                val platform = PNXPlatform()
                platform.load()
                platform.eventManager.callEvent(PlatformInitializationEvent())
                INSTANCE = platform
                return platform
            }

        private fun parseBiome(str: String): PNXBiomeDelegate {
            val id = MappingRegistries.BIOME.get().inverse()[str]
            return PNXAdapter.adapt(id ?: 1)
        }
    }
}
