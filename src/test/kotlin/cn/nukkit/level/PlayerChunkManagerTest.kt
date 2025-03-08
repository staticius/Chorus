package cn.nukkit.level

import cn.nukkit.GameMockExtension
import cn.nukkit.TestPlayer
import cn.nukkit.math.Vector3
import it.unimi.dsi.fastutil.longs.LongArrayPriorityQueue
import it.unimi.dsi.fastutil.longs.LongComparator
import org.apache.commons.lang3.reflect.FieldUtils
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(GameMockExtension::class)
class PlayerChunkManagerTest {
    @Test
    @Throws(IllegalAccessException::class)
    fun test_chunkDistanceComparator(player: TestPlayer) {
        val chunkDistanceComparator = FieldUtils.getDeclaredField(
            PlayerChunkManager::class.java, "chunkDistanceComparator", true
        )
        val playerChunkManager = PlayerChunkManager(player)
        player.setPosition(Vector3(0.0, 100.0, 0.0))


        val longComparator = chunkDistanceComparator[playerChunkManager] as LongComparator
        val longArrayPriorityQueue = LongArrayPriorityQueue(10 * 10, longComparator)
        longArrayPriorityQueue.enqueue(111L)
        longArrayPriorityQueue.enqueue(222L)
        longArrayPriorityQueue.enqueue(22L)
        longArrayPriorityQueue.enqueue(55L)
        val d1 = longArrayPriorityQueue.dequeueLong()
        Assertions.assertEquals(22, d1)
        val d2 = longArrayPriorityQueue.dequeueLong()
        Assertions.assertEquals(55, d2)
        longArrayPriorityQueue.enqueue(d1)
        Assertions.assertEquals(22, longArrayPriorityQueue.dequeueLong())
    }
}
