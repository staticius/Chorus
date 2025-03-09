/**
 * 一些关于异步任务和调度器的类,可用于插件实现异步操作.
 *
 *
 * Classes relevant to asynchronous tasks and schedulers,these can be used by plugins to implement asynchronous operations.
 */
package org.chorus.scheduler

import org.chorus.level.Level.isChunkLoaded
import org.chorus.math.NukkitMath.floorDouble
import org.chorus.level.Level.getBlock
import org.chorus.level.Level.scheduleUpdate
import org.chorus.math.AxisAlignedBB.minX
import org.chorus.math.AxisAlignedBB.maxX
import org.chorus.math.AxisAlignedBB.minZ
import org.chorus.math.AxisAlignedBB.maxZ
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadFactory
import org.chorus.scheduler.AsyncPool
import org.chorus.scheduler.AsyncTask
import org.chorus.utils.ThreadStore
import java.util.concurrent.ConcurrentLinkedQueue
import org.chorus.InterruptibleThread
import java.util.LinkedList
import java.io.ByteArrayInputStream
import java.io.IOException
import org.chorus.scheduler.FileWriteTask
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.ConcurrentHashMap
import org.chorus.scheduler.ServerScheduler
import org.chorus.scheduler.PluginTask
import org.chorus.utils.collection.nb.Long2ObjectNonBlockingMap
import org.chorus.utils.BlockUpdateEntry
import org.chorus.math.NukkitMath
import org.chorus.math.AxisAlignedBB
import java.util.HashSet

