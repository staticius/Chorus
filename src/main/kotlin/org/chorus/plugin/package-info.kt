/**
 * 与插件(一种在运行时加载的软件,可以修改游戏玩法)相关的类.
 *
 *
 * Classes relevant to plugin,the plugin is a software that is loaded at runtime and modifies the gameplay.
 */
package org.chorus.plugin

import cn.nukkit.command.SimpleCommandMap.registerAll
import cn.nukkit.lang.BaseLang.tr
import cn.nukkit.permission.Permission.default
import cn.nukkit.permission.Permissible.recalculatePermissions
import cn.nukkit.command.Command.usage
import cn.nukkit.command.Command.setAliases
import cn.nukkit.level.Level.scheduler
import cn.nukkit.event.HandlerList.Companion.unregisterAll
import cn.nukkit.event.HandlerList.registeredListeners
import cn.nukkit.event.Event.getEventName
import cn.nukkit.event.EventHandler.priority
import cn.nukkit.event.EventHandler.ignoreCancelled
import cn.nukkit.event.HandlerList.register
import cn.nukkit.event.Event.isCancelled
import cn.nukkit.plugin.service.ServicePriority
import cn.nukkit.plugin.service.RegisteredServiceProvider
import java.util.HashMap
import cn.nukkit.command.CommandExecutor
import cn.nukkit.plugin.PluginDescription
import cn.nukkit.plugin.PluginLogger
import cn.nukkit.plugin.InternalPlugin
import cn.nukkit.command.PluginIdentifiableCommand
import cn.nukkit.command.PluginCommand
import java.io.IOException
import cn.nukkit.plugin.PluginBase
import cn.nukkit.utils.ConfigSection
import cn.nukkit.plugin.LibraryLoader
import cn.nukkit.plugin.LibraryLoadException
import java.net.URLClassLoader
import java.net.MalformedURLException
import cn.nukkit.command.SimpleCommandMap
import cn.nukkit.permission.Permissible
import cn.nukkit.plugin.JavaPluginLoader
import java.io.FilenameFilter
import cn.nukkit.lang.BaseLang
import java.util.HashSet
import cn.nukkit.event.HandlerList
import cn.nukkit.plugin.RegisteredListener
import cn.nukkit.plugin.MethodEventExecutor
import cn.nukkit.event.EventPriority
import lombok.NoArgsConstructor
import lombok.AccessLevel
import cn.nukkit.plugin.PluginClassLoader
import java.util.jar.JarFile
import cn.nukkit.event.plugin.PluginEnableEvent
import cn.nukkit.event.plugin.PluginDisableEvent
import cn.nukkit.plugin.PluginLoadOrder
import cn.nukkit.plugin.CompiledExecutor
import java.util.concurrent.atomic.AtomicInteger

