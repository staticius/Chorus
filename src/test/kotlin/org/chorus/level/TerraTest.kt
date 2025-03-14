package org.chorus.level

import org.apache.commons.io.FileUtils
import org.chorus.GameMockExtension
import org.chorus.TestPlayer
import org.chorus.TestUtils
import org.chorus.level.format.LevelConfig
import org.chorus.level.format.LevelConfig.GeneratorConfig
import org.chorus.level.format.leveldb.LevelDBProvider
import org.chorus.level.generator.terra.PNXPlatform
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.nio.file.Path

@ExtendWith(GameMockExtension::class)
class TerraTest {
    /**
     * It is used to test whether the chunk in the player's current position can be loaded normally
     * after teleporting in the Terra generator
     */
    @Test
    fun test_terra(player: TestPlayer) {
        TestUtils.resetPlayerStatus(player)

        player.level = level!!
        player.level.initLevel()
        player.viewDistance = 1

        val loop = TestUtils.gameLoop0(player)

        var limit = 100
        while (limit-- != 0) {
            try {
                if (player.playerChunkManager.usedChunks.size >= 5) {
                    break
                }
                Thread.sleep(100)
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }
        }
        if (limit <= 0) {
            TestUtils.resetPlayerStatus(player)
            Assertions.fail<Any>("Chunks cannot be successfully loaded in 10s")
        }

        //teleport
        player.teleport(player.transform.position.setComponents(10000.0, 100.0, 10000.0))

        var limit2 = 1000
        while (limit2-- != 0) {
            try {
                Thread.sleep(100)
                if (player.chunk != null && player.chunk.chunkState.canSend() && player.chunk.x == 625 && player.chunk.z == 625) {
                    break
                }
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }
        }
        if (limit2 == 0) {
            TestUtils.resetPlayerStatus(player)
            Assertions.fail<Any>("Players are unable to load Terra generator chunks normally")
        }
        loop.stop()
        TestUtils.resetPlayerStatus(player)
    }

    companion object {
        var level: Level? = null

        @BeforeAll
        fun before() {
            val instance = PNXPlatform.getInstance()
            val objectObjectHashMap = HashMap<String, Any>()
            objectObjectHashMap["pack"] = "overworld"
            level = Level(
                "terra",
                "src/test/resources/terra",
                1,
                LevelDBProvider::class.java,
                GeneratorConfig(
                    "terra",
                    114514,
                    false,
                    LevelConfig.AntiXrayMode.LOW,
                    true,
                    DimensionEnum.OVERWORLD.dimensionData,
                    objectObjectHashMap
                )
            )
        }


        @SneakyThrows
        @AfterAll
        fun after() {
            level!!.close()
            val file2 = Path.of("src/test/resources/terra").toFile()
            if (file2.exists()) {
                FileUtils.deleteDirectory(file2)
            }
        }
    }
}
