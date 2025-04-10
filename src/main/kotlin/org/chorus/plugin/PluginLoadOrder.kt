package org.chorus.plugin

/**
 * 描述一个Nukkit插件加载顺序的类。<br></br>
 * Describes a Nukkit plugin load order.
 *
 *
 *
 * Nukkit插件的加载顺序有两个:[org.chorus.plugin.PluginLoadOrder.STARTUP]
 * 和 [org.chorus.plugin.PluginLoadOrder.POSTWORLD]。<br></br>
 * The load order of a Nukkit plugin can be [org.chorus.plugin.PluginLoadOrder.STARTUP]
 * or [org.chorus.plugin.PluginLoadOrder.POSTWORLD].
 */
enum class PluginLoadOrder {
    /**
     * 表示这个插件在服务器启动时就开始加载。<br></br>
     * Indicates that the plugin will be loaded at startup.
     *
     * @see org.chorus.plugin.PluginLoadOrder
     *
     *
     */
    STARTUP,

    /**
     * 表示这个插件在第一个世界加载完成后开始加载。<br></br>
     * Indicates that the plugin will be loaded after the first/default world was created.
     *
     * @see org.chorus.plugin.PluginLoadOrder
     *
     *
     */
    POSTWORLD
}
