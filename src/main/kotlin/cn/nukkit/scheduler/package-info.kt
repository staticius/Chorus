/**
 * 一些关于异步任务和调度器的类,可用于插件实现异步操作.
 *
 *
 * Classes relevant to asynchronous tasks and schedulers,these can be used by plugins to implement asynchronous operations.
 */
package cn.nukkit.scheduler

import cn.nukkit.level.Level.isChunkLoaded
import cn.nukkit.math.NukkitMath.floorDouble
import cn.nukkit.level.Level.getBlock
import cn.nukkit.level.Level.scheduleUpdate
import cn.nukkit.math.AxisAlignedBB.minX
import cn.nukkit.math.AxisAlignedBB.maxX
import cn.nukkit.math.AxisAlignedBB.minZ
import cn.nukkit.math.AxisAlignedBB.maxZ
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadFactory
import cn.nukkit.scheduler.AsyncPool
import cn.nukkit.scheduler.AsyncTask
import cn.nukkit.utils.ThreadStore
import java.util.concurrent.ConcurrentLinkedQueue
import cn.nukkit.InterruptibleThread
import java.util.LinkedList
import java.io.ByteArrayInputStream
import java.io.IOException
import cn.nukkit.scheduler.FileWriteTask
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.ConcurrentHashMap
import cn.nukkit.scheduler.ServerScheduler
import cn.nukkit.scheduler.PluginTask
import cn.nukkit.utils.collection.nb.Long2ObjectNonBlockingMap
import cn.nukkit.utils.BlockUpdateEntry
import cn.nukkit.math.NukkitMath
import cn.nukkit.math.AxisAlignedBB
import java.util.HashSet

