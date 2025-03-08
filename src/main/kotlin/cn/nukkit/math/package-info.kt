/**
 * 一些数学工具类.
 *
 *
 * Some math tool classes.
 */
package cn.nukkit.math

import cn.nukkit.level.Transform.yaw
import cn.nukkit.level.Transform.pitch
import cn.nukkit.math.IVector3
import cn.nukkit.math.ChunkVector2
import cn.nukkit.math.BlockFace
import lombok.SneakyThrows
import cn.nukkit.math.Vector3f
import cn.nukkit.math.BlockVector3
import cn.nukkit.math.BVector3
import cn.nukkit.api.DoNotModify
import cn.nukkit.math.Rotator2
import cn.nukkit.math.NukkitMath
import cn.nukkit.math.Vector2f
import kotlin.math.pow
import cn.nukkit.math.BlockFace.AxisDirection
import cn.nukkit.math.CompassRoseDirection
import java.util.EnumSet
import cn.nukkit.utils.random.RandomSourceProvider
import cn.nukkit.math.MathHelper
import cn.nukkit.utils.random.NukkitRandom
import java.util.LinkedList
import cn.nukkit.math.VectorMath.FixedVector3
import cn.nukkit.math.AxisAlignedBB
import cn.nukkit.math.SimpleAxisAlignedBB
import cn.nukkit.level.MovingObjectPosition
import cn.nukkit.math.AxisAlignedBB.BBConsumer
import lombok.RequiredArgsConstructor
import java.util.concurrent.atomic.AtomicInteger

