/**
 * 与插件(一种在运行时加载的软件,可以修改游戏玩法)相关的类.
 *
 *
 * Classes relevant to plugin,the plugin is a software that is loaded at runtime and modifies the gameplay.
 */
package org.chorus.plugin

import org.chorus.command.SimpleCommandMap.registerAll
import org.chorus.lang.BaseLang.tr
import org.chorus.permission.Permission.default
import org.chorus.permission.Permissible.recalculatePermissions
import org.chorus.command.Command.usage
import org.chorus.command.Command.setAliases
import org.chorus.level.Level.scheduler
import org.chorus.event.HandlerList.Companion.unregisterAll
import org.chorus.event.HandlerList.registeredListeners
import org.chorus.event.Event.getEventName
import org.chorus.event.EventHandler.priority
import org.chorus.event.EventHandler.ignoreCancelled
import org.chorus.event.HandlerList.register
import org.chorus.event.Event.isCancelled
import org.chorus.plugin.service.ServicePriority
import org.chorus.plugin.service.RegisteredServiceProvider
import java.util.HashMap
import org.chorus.command.CommandExecutor
import org.chorus.plugin.PluginDescription
import org.chorus.plugin.PluginLogger
import org.chorus.plugin.InternalPlugin
import org.chorus.command.PluginIdentifiableCommand
import org.chorus.command.PluginCommand
import java.io.IOException
import org.chorus.plugin.PluginBase
import org.chorus.utils.ConfigSection
import org.chorus.plugin.LibraryLoader
import org.chorus.plugin.LibraryLoadException
import java.net.URLClassLoader
import java.net.MalformedURLException
import org.chorus.command.SimpleCommandMap
import org.chorus.permission.Permissible
import org.chorus.plugin.JavaPluginLoader
import java.io.FilenameFilter
import org.chorus.lang.BaseLang
import java.util.HashSet
import org.chorus.event.HandlerList
import org.chorus.plugin.RegisteredListener
import org.chorus.plugin.MethodEventExecutor
import org.chorus.event.EventPriority


import org.chorus.plugin.PluginClassLoader
import java.util.jar.JarFile
import org.chorus.event.plugin.PluginEnableEvent
import org.chorus.event.plugin.PluginDisableEvent
import org.chorus.plugin.PluginLoadOrder
import org.chorus.plugin.CompiledExecutor
import java.util.concurrent.atomic.AtomicInteger

