/**
 * 一些数学工具类.
 *
 *
 * Some math tool classes.
 */
package org.chorus.math

import org.chorus.level.Transform.yaw
import org.chorus.level.Transform.pitch
import org.chorus.math.IVector3
import org.chorus.math.ChunkVector2
import org.chorus.math.BlockFace

import org.chorus.math.Vector3f
import org.chorus.math.BlockVector3
import org.chorus.math.BVector3
import org.chorus.api.DoNotModify
import org.chorus.math.Rotator2
import org.chorus.math.NukkitMath
import org.chorus.math.Vector2f
import kotlin.math.pow
import org.chorus.math.BlockFace.AxisDirection
import org.chorus.math.CompassRoseDirection
import java.util.EnumSet
import org.chorus.utils.random.RandomSourceProvider
import org.chorus.math.MathHelper
import org.chorus.utils.random.NukkitRandom
import java.util.LinkedList
import org.chorus.math.VectorMath.FixedVector3
import org.chorus.math.AxisAlignedBB
import org.chorus.math.SimpleAxisAlignedBB
import org.chorus.level.MovingObjectPosition
import org.chorus.math.AxisAlignedBB.BBConsumer

import java.util.concurrent.atomic.AtomicInteger

