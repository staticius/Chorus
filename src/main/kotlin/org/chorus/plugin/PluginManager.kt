package org.chorus.plugin

import cn.nukkit.Server
import cn.nukkit.command.PluginCommand
import cn.nukkit.command.SimpleCommandMap
import cn.nukkit.event.*
import cn.nukkit.event.HandlerList.Companion.unregisterAll
import cn.nukkit.level.Level
import cn.nukkit.permission.Permissible
import cn.nukkit.permission.Permission
import cn.nukkit.utils.PluginException
import cn.nukkit.utils.Utils
import io.netty.util.internal.EmptyArrays
import lombok.Getter
import lombok.extern.slf4j.Slf4j
import java.io.File
import java.lang.Deprecated
import java.lang.reflect.Method
import java.util.*
import java.util.function.Consumer
import java.util.regex.Pattern
import kotlin.Any
import kotlin.Array
import kotlin.Boolean
import kotlin.Exception
import kotlin.IllegalArgumentException
import kotlin.NullPointerException
import kotlin.String
import kotlin.Throwable
import kotlin.Throws
import kotlin.UnsupportedOperationException
import kotlin.addSuppressed
import kotlin.also
import kotlin.require

/**
 * @author MagicDroidX
 */
@Slf4j
open class PluginManager(private val server: Server, private val commandMap: SimpleCommandMap) {
    protected val plugins: MutableMap<String?, Plugin> = LinkedHashMap()

    protected val permissions: MutableMap<String, Permission> = HashMap()

    protected val defaultPerms: MutableMap<String, Permission> = HashMap()

    protected val defaultPermsOp: MutableMap<String, Permission> = HashMap()

    protected val permSubs: MutableMap<String, MutableSet<Permissible>> = HashMap()

    protected val defSubs: MutableSet<Permissible> = Collections.newSetFromMap(WeakHashMap())

    protected val defSubsOp: MutableSet<Permissible> = Collections.newSetFromMap(WeakHashMap())

    @Getter
    protected val fileAssociations: MutableMap<String, PluginLoader> = HashMap()

    fun getPlugin(name: String?): Plugin? {
        if (plugins.containsKey(name)) {
            return plugins[name]
        }
        return null
    }

    fun registerInterface(loaderClass: Class<out PluginLoader>?): Boolean {
        if (loaderClass != null) {
            try {
                val constructor = loaderClass.getDeclaredConstructor(
                    Server::class.java
                )
                constructor.isAccessible = true
                fileAssociations[loaderClass.name] = constructor.newInstance(this.server)
                return true
            } catch (e: Exception) {
                return false
            }
        }
        return false
    }

    fun loadInternalPlugin() {
        val pluginLoader = fileAssociations[JavaPluginLoader::class.java.name]
        val plugin: InternalPlugin = InternalPlugin.Companion.INSTANCE
        val info: MutableMap<String, Any> = HashMap()
        info["name"] = "PowerNukkitX"
        info["version"] = server.nukkitVersion
        info["website"] = "https://github.com/PowerNukkitX/PowerNukkitX"
        info["main"] = InternalPlugin::class.java.name
        var file = try {
            File(Server::class.java.protectionDomain.codeSource.location.toURI())
        } catch (e: Exception) {
            File(".")
        }
        val description = PluginDescription(info)
        plugin.init(
            pluginLoader,
            javaClass.classLoader, server, description, File("PowerNukkitX"), file
        )
        plugins[description.name] = plugin
        enablePlugin(plugin)
    }

    fun getPlugins(): Map<String?, Plugin> {
        return plugins
    }

    fun loadPlugin(file: File): Plugin? {
        return this.loadPlugin(file, null)
    }

    @JvmOverloads
    fun loadPlugin(path: String, loaders: Map<String, PluginLoader>? = null): Plugin? {
        return this.loadPlugin(File(path), loaders)
    }

    fun loadPlugin(file: File, loaders: Map<String, PluginLoader>?): Plugin? {
        for (loader in (loaders ?: this.fileAssociations).values) {
            for (pattern in loader.pluginFilters) {
                if (pattern.matcher(file.name).matches()) {
                    val description = loader.getPluginDescription(file)
                    if (description != null) {
                        try {
                            val plugin = loader.loadPlugin(file)
                            if (plugin != null) {
                                plugins[plugin.description.name] = plugin

                                val pluginCommands = this.parseYamlCommands(plugin)

                                if (!pluginCommands.isEmpty()) {
                                    commandMap.registerAll(plugin.description.name, pluginCommands)
                                }

                                return plugin
                            }
                        } catch (e: Exception) {
                            PluginManager.log.error("Could not load plugin", e)
                            return null
                        }
                    }
                }
            }
        }

        return null
    }

    fun loadPlugins(dictionary: String): Map<String?, Plugin> {
        return this.loadPlugins(File(dictionary))
    }

    fun loadPlugins(dictionary: String, newLoaders: List<String>?): Map<String?, Plugin> {
        return this.loadPlugins(File(dictionary), newLoaders)
    }

    @JvmOverloads
    fun loadPlugins(dictionary: File, newLoaders: List<String>? = null): Map<String?, Plugin> {
        return this.loadPlugins(dictionary, newLoaders, false)
    }

    fun loadPlugins(dictionary: File, newLoaders: List<String>?, includeDir: Boolean): Map<String?, Plugin> {
        if (dictionary.isDirectory) {
            val plugins: MutableMap<String, File> = LinkedHashMap()
            val loadedPlugins: MutableMap<String?, Plugin> = LinkedHashMap()
            val dependencies: MutableMap<String?, MutableList<String?>?> = LinkedHashMap()
            val softDependencies: MutableMap<String?, MutableList<String?>?> = LinkedHashMap()
            var loaders: MutableMap<String, PluginLoader> = LinkedHashMap()
            if (newLoaders != null) {
                for (key in newLoaders) {
                    if (fileAssociations.containsKey(key)) {
                        loaders[key] = fileAssociations[key]!!
                    }
                }
            } else {
                loaders = this.fileAssociations
            }

            for (loader in loaders.values) {
                for (file in Objects.requireNonNull<Array<File>>(dictionary.listFiles { dir: File?, name: String? ->
                    for (pattern in loader.pluginFilters) {
                        if (pattern!!.matcher(name).matches()) {
                            return@listFiles true
                        }
                    }
                    false
                })) {
                    if ((file.isDirectory && !file.name.startsWith("@")) && !includeDir) {
                        continue
                    }
                    try {
                        val description = loader.getPluginDescription(file)
                        if (description != null) {
                            val name = description.name

                            if (plugins.containsKey(name) || this.getPlugin(name) != null) {
                                PluginManager.log.error(
                                    server.language.tr(
                                        "nukkit.plugin.duplicateError",
                                        name!!
                                    )
                                )
                                continue
                            }

                            var compatible = 0

                            for (version in description.compatibleAPIs) {
                                try {
                                    //Check the format: majorVersion.minorVersion.patch
                                    require(
                                        Pattern.matches(
                                            "^[0-9]+\\.[0-9]+\\.[0-9]+$",
                                            version
                                        )
                                    ) { "The getCompatibleAPI version don't match the format majorVersion.minorVersion.patch" }
                                } catch (e: NullPointerException) {
                                    PluginManager.log.error(
                                        server.language.tr(
                                            "nukkit.plugin.loadError",
                                            name!!, "Wrong API format"
                                        ), e
                                    )
                                    continue
                                } catch (e: IllegalArgumentException) {
                                    PluginManager.log.error(
                                        server.language.tr(
                                            "nukkit.plugin.loadError",
                                            name!!, "Wrong API format"
                                        ), e
                                    )
                                    continue
                                }

                                val versionArray =
                                    version.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                val apiVersion =
                                    server.apiVersion.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()

                                //Completely different API version
                                if (versionArray[0].toInt() != apiVersion[0].toInt()) {
                                    compatible = 1
                                    break
                                }

                                //If the plugin requires new API features, being backwards compatible
                                if (versionArray[1].toInt() > apiVersion[1].toInt()) {
                                    compatible = 2
                                    continue
                                }
                                break
                            }

                            if (compatible > 0) {
                                PluginManager.log.error(
                                    server.language.tr(
                                        "nukkit.plugin.loadError",
                                        name!!, "%nukkit.plugin.incompatibleAPI"
                                    )
                                )
                                if (compatible == 1) {
                                    PluginManager.log.error("The major version is not compatible, and the plugin will not load!")
                                    continue
                                }
                            }

                            plugins[name!!] = file

                            softDependencies[name] = description.softDepend

                            dependencies[name] = description.depend

                            for (before in description.loadBefore) {
                                if (softDependencies.containsKey(before)) {
                                    softDependencies[before]!!.add(name)
                                } else {
                                    val list: MutableList<String?> = ArrayList()
                                    list.add(name)
                                    softDependencies[before] = list
                                }
                            }
                        }
                    } catch (e: Exception) {
                        PluginManager.log.error(
                            server.language.tr(
                                "nukkit.plugin" +
                                        ".fileError", file.name, dictionary.toString(), Utils
                                    .getExceptionMessage(e)
                            ), e
                        )
                    }
                }
            }

            while (!plugins.isEmpty()) {
                var missingDependency = true
                for (name in ArrayList<String>(plugins.keys)) {
                    val file = plugins[name]
                    if (dependencies.containsKey(name)) {
                        for (dependency in ArrayList<String>(dependencies[name])) {
                            if (loadedPlugins.containsKey(dependency) || this.getPlugin(dependency) != null) {
                                dependencies[name]!!.remove(dependency)
                            } else if (!plugins.containsKey(dependency)) {
                                val language = server.language
                                val cause = language.tr("nukkit.plugin.missingDependency", dependency)
                                PluginManager.log.error(language.tr("nukkit.plugin.loadError", name, cause))
                                break
                            }
                        }

                        if (dependencies[name]!!.isEmpty()) {
                            dependencies.remove(name)
                        }
                    }

                    if (softDependencies.containsKey(name)) {
                        softDependencies[name]!!.removeIf { dependency: String? ->
                            loadedPlugins.containsKey(dependency) || this.getPlugin(
                                dependency
                            ) != null
                        }

                        if (softDependencies[name]!!.isEmpty()) {
                            softDependencies.remove(name)
                        }
                    }

                    if (!dependencies.containsKey(name) && !softDependencies.containsKey(name)) {
                        plugins.remove(name)
                        missingDependency = false
                        val plugin = this.loadPlugin(file!!, loaders)
                        if (plugin != null) {
                            loadedPlugins[name] = plugin
                        } else {
                            PluginManager.log.error(server.language.tr("nukkit.plugin.genericLoadError", name))
                        }
                    }
                }

                if (missingDependency) {
                    for (name in ArrayList<String>(plugins.keys)) {
                        val file = plugins[name]
                        if (!dependencies.containsKey(name)) {
                            softDependencies.remove(name)
                            plugins.remove(name)
                            missingDependency = false
                            val plugin = this.loadPlugin(file!!, loaders)
                            if (plugin != null) {
                                loadedPlugins[name] = plugin
                            } else {
                                PluginManager.log.error(server.language.tr("nukkit.plugin.genericLoadError", name))
                            }
                        }
                    }

                    if (missingDependency) {
                        for (name in plugins.keys) {
                            PluginManager.log.error(
                                server.language.tr(
                                    "nukkit.plugin.loadError",
                                    name,
                                    "%nukkit.plugin.circularDependency"
                                )
                            )
                        }
                        plugins.clear()
                    }
                }
            }

            return loadedPlugins
        } else {
            return HashMap()
        }
    }

    fun getPermission(name: String): Permission? {
        if (permissions.containsKey(name)) {
            return permissions[name]
        }
        return null
    }

    fun addPermission(permission: Permission): Boolean {
        if (!permissions.containsKey(permission.name)) {
            permissions[permission.name] = permission
            this.calculatePermissionDefault(permission)

            return true
        }

        return false
    }

    fun removePermission(name: String) {
        permissions.remove(name)
    }

    fun removePermission(permission: Permission) {
        this.removePermission(permission.name)
    }

    fun getDefaultPermissions(op: Boolean): Map<String, Permission> {
        return if (op) {
            defaultPermsOp
        } else {
            defaultPerms
        }
    }

    fun recalculatePermissionDefaults(permission: Permission) {
        if (permissions.containsKey(permission.name)) {
            defaultPermsOp.remove(permission.name)
            defaultPerms.remove(permission.name)
            this.calculatePermissionDefault(permission)
        }
    }

    private fun calculatePermissionDefault(permission: Permission) {
        if (permission.default == Permission.DEFAULT_OP || permission.default == Permission.DEFAULT_TRUE) {
            defaultPermsOp[permission.name] = permission
            this.dirtyPermissibles(true)
        }

        if (permission.default == Permission.DEFAULT_NOT_OP || permission.default == Permission.DEFAULT_TRUE) {
            defaultPerms[permission.name] = permission
            this.dirtyPermissibles(false)
        }
    }

    private fun dirtyPermissibles(op: Boolean) {
        for (p in this.getDefaultPermSubscriptions(op)) {
            p.recalculatePermissions()
        }
    }

    fun subscribeToPermission(permission: String, permissible: Permissible) {
        if (!permSubs.containsKey(permission)) {
            permSubs[permission] =
                Collections.newSetFromMap(WeakHashMap())
        }
        permSubs[permission]!!.add(permissible)
    }

    fun unsubscribeFromPermission(permission: String, permissible: Permissible) {
        if (permSubs.containsKey(permission)) {
            permSubs[permission]!!.remove(permissible)
            if (permSubs[permission]!!.isEmpty()) {
                permSubs.remove(permission)
            }
        }
    }

    fun getPermissionSubscriptions(permission: String): Set<Permissible> {
        if (permSubs.containsKey(permission)) {
            return HashSet(permSubs[permission])
        }
        return HashSet()
    }

    fun subscribeToDefaultPerms(op: Boolean, permissible: Permissible) {
        if (op) {
            defSubsOp.add(permissible)
        } else {
            defSubs.add(permissible)
        }
    }

    fun unsubscribeFromDefaultPerms(op: Boolean, permissible: Permissible) {
        if (op) {
            defSubsOp.remove(permissible)
        } else {
            defSubs.remove(permissible)
        }
    }

    fun getDefaultPermSubscriptions(op: Boolean): Set<Permissible> {
        return if (op) {
            HashSet(this.defSubsOp)
        } else {
            HashSet(this.defSubs)
        }
    }

    fun getPermissions(): Map<String, Permission> {
        return permissions
    }

    fun isPluginEnabled(plugin: Plugin?): Boolean {
        return if (plugin != null && plugins.containsKey(plugin.description.name)) {
            plugin.isEnabled
        } else {
            false
        }
    }

    fun enablePlugin(plugin: Plugin) {
        if (!plugin.isEnabled) {
            try {
                for (permission in plugin.description.permissions) {
                    this.addPermission(permission)
                }
                plugin.pluginLoader.enablePlugin(plugin)
            } catch (e: Throwable) {
                PluginManager.log.error(
                    "An error occurred while enabling the plugin {}, {}, {}",
                    plugin.description.name, plugin.description.version, plugin.description.main, e
                )
                this.disablePlugin(plugin)
            }
        }
    }

    protected fun parseYamlCommands(plugin: Plugin): List<PluginCommand<*>> {
        val pluginCmds: MutableList<PluginCommand<*>> = ArrayList()

        for ((key1, value) in plugin.description.commands) {
            val key = key1 as String
            val data = value!!
            if (key.contains(":")) {
                PluginManager.log.error(
                    server.language.tr(
                        "nukkit.plugin.commandError",
                        key,
                        plugin.description.fullName
                    )
                )
                continue
            }
            if (data is Map<*, *>) {
                val newCmd = PluginCommand(key, plugin)

                if (data.containsKey("description")) {
                    newCmd.description = (data["description"] as String?)!!
                }

                if (data.containsKey("usage")) {
                    newCmd.usage = (data["usage"] as String?)!!
                }

                if (data.containsKey("aliases")) {
                    val aliases = data["aliases"]
                    if (aliases is List<*>) {
                        val aliasList: MutableList<String> = ArrayList()
                        for (alias in aliases) {
                            if (alias.contains(":")) {
                                PluginManager.log.error(
                                    server.language.tr(
                                        "nukkit.plugin.aliasError",
                                        alias,
                                        plugin.description.fullName
                                    )
                                )
                                continue
                            }
                            aliasList.add(alias)
                        }

                        newCmd.setAliases(aliasList.toArray(EmptyArrays.EMPTY_STRINGS))
                    }
                }

                if (data.containsKey("permission")) {
                    newCmd.permission = data["permission"]
                }

                if (data.containsKey("permission-message")) {
                    newCmd.permissionMessage = data["permission-message"]
                }

                pluginCmds.add(newCmd)
            }
        }

        return pluginCmds
    }

    fun disablePlugins() {
        val plugins: ListIterator<Plugin> = ArrayList(
            getPlugins().values
        ).listIterator(getPlugins().size)

        while (plugins.hasPrevious()) {
            val previous = plugins.previous()
            if (previous !== InternalPlugin.Companion.INSTANCE) {
                this.disablePlugin(previous)
            }
        }
    }

    fun disablePlugin(plugin: Plugin) {
        if (InternalPlugin.Companion.INSTANCE == plugin) {
            throw UnsupportedOperationException("The PowerNukkitX Internal plugin can't be disabled.")
        }

        if (plugin.isEnabled) {
            try {
                plugin.pluginLoader.disablePlugin(plugin)
            } catch (e: Exception) {
                PluginManager.log.error(
                    "An error occurred while disabling the plugin {}, {}, {}",
                    plugin.description.name, plugin.description.version, plugin.description.main, e
                )
            }

            server.scheduler.cancelTask(plugin)
            server.levels.values.forEach(Consumer { level: Level -> level.scheduler.cancelTask(plugin) })
            unregisterAll(plugin)
            for (permission in plugin.description.permissions) {
                this.removePermission(permission)
            }
        }
    }

    fun clearPlugins() {
        this.disablePlugins()
        plugins.clear()
        fileAssociations.clear()
        permissions.clear()
        defaultPerms.clear()
        defaultPermsOp.clear()
    }

    open fun callEvent(event: Event) {
        //Used for event listeners inside command blocks
        try {
            for (registration in getEventListeners(event.javaClass).registeredListeners) {
                if (!registration.plugin.isEnabled) {
                    continue
                }

                try {
                    registration.callEvent(event)
                } catch (e: Exception) {
                    PluginManager.log.error(
                        server.language.tr(
                            "nukkit.plugin.eventError", event.getEventName(), registration.plugin.description.fullName,
                            e.message!!, registration.listener.javaClass.name
                        ), e
                    )
                }
            }
        } catch (e: IllegalAccessException) {
            PluginManager.log.error("An error has occurred while calling the event {}", event, e)
        }
    }

    fun registerEvents(listener: Listener, plugin: Plugin) {
        if (!plugin.isEnabled) {
            throw PluginException("Plugin attempted to register " + listener.javaClass.name + " while not enabled")
        }

        val methods: Set<Method>
        try {
            val publicMethods = listener.javaClass.methods
            val privateMethods = listener.javaClass.declaredMethods
            methods = HashSet(publicMethods.size + privateMethods.size, 1.0f)
            Collections.addAll(methods, *publicMethods)
            Collections.addAll(methods, *privateMethods)
        } catch (e: NoClassDefFoundError) {
            plugin.logger.error("Plugin " + plugin.description.fullName + " has failed to register events for " + listener.javaClass + " because " + e.message + " does not exist.")
            return
        }

        for (method in methods) {
            val eh =
                method.getAnnotation(EventHandler::class.java) ?: continue
            if (method.isBridge || method.isSynthetic) {
                continue
            }
            val checkClass: Class<*>

            if (method.parameterTypes.size != 1 || !Event::class.java.isAssignableFrom(method.parameterTypes[0].also {
                    checkClass = it
                })) {
                plugin.logger.error(plugin.description.fullName + " attempted to register an invalid EventHandler method signature \"" + method.toGenericString() + "\" in " + listener.javaClass)
                continue
            }

            val eventClass = checkClass.asSubclass(Event::class.java)
            method.isAccessible = true

            var clazz: Class<*> = eventClass
            while (Event::class.java.isAssignableFrom(clazz)) {
                // This loop checks for extending deprecated events
                if (clazz.getAnnotation<Deprecated?>(Deprecated::class.java) != null) {
                    if (java.lang.String.valueOf(server.settings.baseSettings().deprecatedVerbose()).toBoolean()) {
                        PluginManager.log.warn(
                            server.language.tr(
                                "nukkit.plugin.deprecatedEvent",
                                plugin.name,
                                clazz.name,
                                listener.javaClass.name + "." + method.name + "()"
                            )
                        )
                    }
                    break
                }
                clazz = clazz.superclass
            }
            var eventExecutor: EventExecutor = MethodEventExecutor.Companion.compile(listener.javaClass, method)
            if (eventExecutor == null) {
                eventExecutor = MethodEventExecutor(method)
                PluginManager.log.debug("Compile fast EventExecutor {} for {} failed!", eventClass.name, plugin.name)
            }
            this.registerEvent(eventClass, listener, eh.priority, eventExecutor, plugin, eh.ignoreCancelled)
        }
    }

    @JvmOverloads
    @Throws(PluginException::class)
    fun registerEvent(
        event: Class<out Event>,
        listener: Listener,
        priority: EventPriority?,
        executor: EventExecutor,
        plugin: Plugin,
        ignoreCancelled: Boolean = false
    ) {
        if (!plugin.isEnabled) {
            throw PluginException("Plugin attempted to register $event while not enabled")
        }

        try {
            getEventListeners(event).register(RegisteredListener(listener, executor, priority, plugin, ignoreCancelled))
        } catch (e: IllegalAccessException) {
            PluginManager.log.error(
                "An error occurred while registering the event listener event:{}, listener:{} for plugin:{} version:{}",
                event, listener, plugin.description.name, plugin.description.version, e
            )
        }
    }

    @Throws(IllegalAccessException::class)
    private fun getEventListeners(type: Class<out Event>): HandlerList {
        try {
            val method = getRegistrationClass(type).getDeclaredMethod("getHandlers")
            method.isAccessible = true
            return method.invoke(null) as HandlerList
        } catch (e: NullPointerException) {
            throw IllegalArgumentException("getHandlers method in " + type.name + " was not static!", e)
        } catch (e: Exception) {
            val illegalAccessException = IllegalAccessException(Utils.getExceptionMessage(e))
            illegalAccessException.addSuppressed(e)
            throw illegalAccessException
        }
    }

    @Throws(IllegalAccessException::class)
    private fun getRegistrationClass(clazz: Class<out Event>): Class<out Event> {
        try {
            clazz.getDeclaredMethod("getHandlers")
            return clazz
        } catch (e: NoSuchMethodException) {
            if (clazz.superclass != null && (clazz.superclass != Event::class.java) && Event::class.java.isAssignableFrom(
                    clazz.superclass
                )
            ) {
                return getRegistrationClass(clazz.superclass.asSubclass(Event::class.java))
            } else {
                throw IllegalAccessException("Unable to find handler list for event " + clazz.name + ". Static getHandlers method required!")
            }
        }
    }
}
