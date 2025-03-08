package cn.nukkit.level.format.leveldb

import cn.nukkit.api.UsedByReflection
import cn.nukkit.blockentity.BlockEntitySpawnable
import cn.nukkit.level.*
import cn.nukkit.level.format.*
import cn.nukkit.level.format.LevelConfig.GeneratorConfig
import cn.nukkit.level.format.leveldb.LevelDat.*
import cn.nukkit.math.BlockVector3
import cn.nukkit.math.Vector3
import cn.nukkit.nbt.NBTIO
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.IntTag
import cn.nukkit.network.protocol.types.GameType
import cn.nukkit.utils.*
import cn.nukkit.utils.collection.nb.Long2ObjectNonBlockingMap
import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.ByteBufOutputStream
import it.unimi.dsi.fastutil.Pair
import lombok.extern.slf4j.Slf4j
import org.iq80.leveldb.*
import java.io.*
import java.lang.ref.WeakReference
import java.nio.ByteOrder
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.*
import java.util.concurrent.atomic.AtomicReference

/**
 * @author CoolLoong (PNX Project)
 */
@Slf4j
class LevelDBProvider(level: Level, override val path: String) : LevelProvider {
    private val lastChunk = ThreadLocal<WeakReference<IChunk?>>()
    protected val chunks: Long2ObjectNonBlockingMap<IChunk> = Long2ObjectNonBlockingMap()
    var levelData: LevelDat? = null
    protected val storage: LevelDBStorage
    override val level: Level?

    init {
        this.storage = CACHE.computeIfAbsent(path) { p: String ->
            try {
                return@computeIfAbsent LevelDBStorage(
                    level.dimensionCount, p, Options()
                        .createIfMissing(true)
                        .compressionType(CompressionType.ZLIB_RAW)
                        .blockSize(64 * 1024)
                )
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
        this.level = level
        var levelDat = readLevelDat()
        if (levelDat == null) {
            levelDat = LevelDat.builder().build()
            this.levelData = levelDat
            saveLevelData()
        } else {
            this.levelData = levelDat
        }
    }

    fun loadChunk(index: Long, chunkX: Int, chunkZ: Int, create: Boolean): IChunk? {
        var chunk: IChunk?
        try {
            chunk = storage.readChunk(chunkX, chunkZ, this)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        if (chunk == null) {
            if (create) {
                chunk = this.getEmptyChunk(chunkX, chunkZ)
                putChunk(index, chunk)
            }
        } else {
            putChunk(index, chunk)
        }
        return chunk
    }

    fun size(): Int {
        return chunks.size
    }

    override fun unloadChunks() {
        val iter = chunks.values.iterator()
        while (iter.hasNext()) {
            iter.next().unload(true, false)
            iter.remove()
        }
    }

    override val loadedChunks: Map<Long, IChunk>
        get() = Collections.unmodifiableMap(chunks)

    override fun isChunkLoaded(X: Int, Z: Int): Boolean {
        return isChunkLoaded(Level.Companion.chunkHash(X, Z))
    }

    fun putChunk(index: Long, chunk: IChunk?) {
        chunks.put(index, chunk)
    }

    override fun isChunkLoaded(hash: Long): Boolean {
        return chunks.containsKey(hash)
    }

    override fun setChunk(chunkX: Int, chunkZ: Int, chunk: IChunk) {
        chunk.setPosition(chunkX, chunkZ)
        val index: Long = Level.Companion.chunkHash(chunkX, chunkZ)
        if (chunks.containsKey(index) && chunks[index] != chunk) {
            this.unloadChunk(chunkX, chunkZ, false)
        }
        lastChunk.remove() //remove cache
        putChunk(index, chunk)
    }

    override val dimensionData: DimensionData
        get() = level.getDimensionData()

    override fun requestChunkData(x: Int, z: Int): Pair<ByteArray, Int> {
        val chunk = this.getChunk(x, z, false) ?: throw ChunkException("Invalid Chunk Set")
        val data = AtomicReference<ByteArray>()
        val subChunkCountRef = AtomicReference<Int>()
        chunk.batchProcess { unsafeChunk: UnsafeChunk ->
            val byteBuf = ByteBufAllocator.DEFAULT.ioBuffer()
            try {
                val sections = unsafeChunk.sections
                var subChunkCount = unsafeChunk.dimensionData.chunkSectionCount
                while (subChunkCount-- != 0) {
                    if (sections!![subChunkCount] != null) {
                        break
                    }
                }
                val total = subChunkCount + 1
                //write block
                if (level != null && level.isAntiXrayEnabled) {
                    for (i in 0..<total) {
                        if (sections!![i] == null) {
                            sections[i] = ChunkSection((i + dimensionData.minSectionY).toByte())
                        }
                        checkNotNull(sections[i])
                        sections[i]!!.writeObfuscatedToBuf(level, byteBuf)
                    }
                } else {
                    for (i in 0..<total) {
                        if (sections!![i] == null) {
                            sections[i] = ChunkSection((i + dimensionData.minSectionY).toByte())
                        }
                        checkNotNull(sections[i])
                        sections[i]!!.writeToBuf(byteBuf)
                    }
                }

                // Write biomes
                for (i in 0..<total) {
                    sections!![i]!!.biomes.writeToNetwork(byteBuf) { obj: V? -> obj.toInt() }
                }

                byteBuf.writeByte(0) // edu- border blocks

                // Block entities
                val tagList: MutableList<CompoundTag> = ArrayList()
                for (blockEntity in unsafeChunk.blockEntities.values) {
                    if (blockEntity is BlockEntitySpawnable) {
                        tagList.add(blockEntity.spawnCompound)
                        //Adding NBT to a chunk pack does not show some block entities, and you have to send block entity packets to the player
                        level!!.addChunkPacket(
                            blockEntity.position.chunkX,
                            blockEntity.position.chunkZ,
                            blockEntity.spawnPacket
                        )
                    }
                }
                try {
                    ByteBufOutputStream(byteBuf).use { stream ->
                        NBTIO.write(tagList, stream, ByteOrder.LITTLE_ENDIAN, true)
                    }
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
                data.set(Utils.convertByteBuf2Array(byteBuf))
                subChunkCountRef.set(total)
            } finally {
                byteBuf.release()
            }
        }
        return Pair.of(data.get(), subChunkCountRef.get())
    }


    override val name: String?
        get() = levelData.getName()

    override var isRaining: Boolean
        get() = levelData.isRaining()
        set(raining) {
            levelData!!.isRaining = raining
        }

    override var rainTime: Int
        get() = levelData.getRainTime()
        set(rainTime) {
            levelData!!.setRainTime(rainTime)
        }

    override var isThundering: Boolean
        get() = levelData.isThundering()
        set(thundering) {
            levelData!!.isThundering = thundering
        }

    override var thunderTime: Int
        get() = levelData.getLightningTime()
        set(thunderTime) {
            levelData!!.setLightningTime(thunderTime)
        }

    override var currentTick: Long
        get() = levelData.getCurrentTick()
        set(currentTick) {
            levelData!!.setCurrentTick(currentTick)
        }

    override var time: Long
        get() = levelData.getTime()
        set(value) {
            levelData.setTime(value)
        }

    override var seed: Long
        get() = levelData.getRandomSeed()
        set(value) {
            levelData!!.setRandomSeed(value)
        }

    override var spawn: Vector3?
        get() = levelData.getSpawnPoint().asVector3().add(0.5, 0.0, 0.5)
        set(pos) {
            levelData.setSpawnPoint(BlockVector3(pos!!.south.toInt(), pos.up.toInt(), pos.west.toInt()))
        }

    override val gamerules: GameRules?
        get() = levelData.getGameRules()

    override fun setGameRules(rules: GameRules?) {
        levelData.setGameRules(rules)
    }

    override fun saveChunks() {
        for (chunk in chunks.values) {
            if (chunk.changes != 0L) {
                chunk.setChanged(false)
                this.saveChunk(chunk.x, chunk.z)
            }
        }
    }

    override fun saveChunk(X: Int, Z: Int) {
        val chunk = this.getChunk(X, Z)
        if (chunk != null) {
            try {
                storage.writeChunk(chunk)
            } catch (e: Exception) {
                throw ChunkException("Error saving chunk ($X, $Z)", e)
            }
        }
    }

    override fun saveChunk(X: Int, Z: Int, chunk: IChunk) {
        chunk.x = X
        chunk.z = Z
        try {
            storage.writeChunk(chunk)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    override fun saveLevelData() {
        writeLevelDat(path, dimensionData, levelData!!)
    }

    override fun updateLevelName(name: String) {
        if (this.name != name) {
            levelData.setName(name)
        }
    }

    override fun loadChunk(chunkX: Int, chunkZ: Int): Boolean {
        return this.loadChunk(chunkX, chunkZ, false)
    }

    override fun loadChunk(chunkX: Int, chunkZ: Int, create: Boolean): Boolean {
        val index: Long = Level.Companion.chunkHash(chunkX, chunkZ)
        if (chunks.containsKey(index)) {
            return true
        }
        return loadChunk(index, chunkX, chunkZ, create) != null
    }

    override fun unloadChunk(X: Int, Z: Int): Boolean {
        return this.unloadChunk(X, Z, true)
    }

    override fun unloadChunk(X: Int, Z: Int, safe: Boolean): Boolean {
        val index: Long = Level.Companion.chunkHash(X, Z)
        val chunk = chunks[index]
        if (chunk != null && chunk.unload(false, safe)) {
            lastChunk.remove()
            chunks.remove(index, chunk)
            return true
        }
        return false
    }

    override fun getChunk(chunkX: Int, chunkZ: Int): IChunk? {
        return this.getChunk(chunkX, chunkZ, false)
    }

    protected val threadLastChunk: IChunk?
        get() {
            val ref = lastChunk.get() ?: return null
            return ref.get()
        }

    override fun getLoadedChunk(chunkX: Int, chunkZ: Int): IChunk? {
        var tmp = threadLastChunk
        if (tmp != null && tmp.x == chunkX && tmp.z == chunkZ) {
            return tmp
        }
        val index: Long = Level.Companion.chunkHash(chunkX, chunkZ)
        lastChunk.set(WeakReference(chunks[index].also { tmp = it }))
        return tmp
    }

    override fun getLoadedChunk(hash: Long): IChunk? {
        var tmp = threadLastChunk
        if (tmp != null && tmp.index == hash) {
            return tmp
        }
        lastChunk.set(WeakReference(chunks[hash].also { tmp = it }))
        return tmp
    }

    override fun getChunk(chunkX: Int, chunkZ: Int, create: Boolean): IChunk? {
        val index: Long = Level.Companion.chunkHash(chunkX, chunkZ)
        var tmp = getLoadedChunk(index)
        if (tmp == null) {
            tmp = this.loadChunk(index, chunkX, chunkZ, create)
            lastChunk.set(WeakReference(tmp))
        }
        return tmp
    }

    override fun getEmptyChunk(x: Int, z: Int): IChunk {
        return Chunk.Companion.builder().levelProvider(this).emptyChunk(x, z)
    }

    override fun isChunkPopulated(chunkX: Int, chunkZ: Int): Boolean {
        val chunk = this.getChunk(chunkX, chunkZ)
        return chunk != null && chunk.chunkState.ordinal >= 2
    }

    override fun close() {
        storage.close()
    }

    override fun isChunkGenerated(chunkX: Int, chunkZ: Int): Boolean {
        return true
    }

    @Synchronized
    @Throws(IOException::class)
    fun readLevelDat(): LevelDat? {
        val levelDat = Path.of(path).resolve("level.dat").toFile()
        if (!levelDat.exists()) return null
        try {
            FileInputStream(levelDat).use { input ->
                //The first 8 bytes are magic number
                input.skip(8)
                val stream = BufferedInputStream(ByteArrayInputStream(input.readAllBytes()))
                val d = NBTIO.read(stream, ByteOrder.LITTLE_ENDIAN)
                stream.close()
                val abilities = d.getCompound("abilities")
                val experiments = d.getCompound("experiments")
                val gameRules: GameRules = GameRules.Companion.getDefault()
                gameRules.setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, d.getBoolean("bonusChestSpawned"))
                gameRules.setGameRule(GameRule.COMMAND_BLOCKS_ENABLED, d.getBoolean("commandblocksenabled"))
                gameRules.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, d.getBoolean("dodaylightcycle"))
                gameRules.setGameRule(GameRule.DO_ENTITY_DROPS, d.getBoolean("doentitydrops"))
                gameRules.setGameRule(GameRule.DO_FIRE_TICK, d.getBoolean("dofiretick"))
                gameRules.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, d.getBoolean("doimmediaterespawn"))
                gameRules.setGameRule(GameRule.DO_INSOMNIA, d.getBoolean("doinsomnia"))
                gameRules.setGameRule(GameRule.DO_LIMITED_CRAFTING, d.getBoolean("dolimitedcrafting"))
                gameRules.setGameRule(GameRule.DO_MOB_LOOT, d.getBoolean("domobloot"))
                gameRules.setGameRule(GameRule.DO_MOB_SPAWNING, d.getBoolean("domobspawning"))
                gameRules.setGameRule(GameRule.DO_TILE_DROPS, d.getBoolean("dotiledrops"))
                gameRules.setGameRule(GameRule.DO_WEATHER_CYCLE, d.getBoolean("doweathercycle"))
                gameRules.setGameRule(GameRule.DROWNING_DAMAGE, d.getBoolean("drowningdamage"))
                gameRules.setGameRule(GameRule.FALL_DAMAGE, d.getBoolean("falldamage"))
                gameRules.setGameRule(GameRule.FIRE_DAMAGE, d.getBoolean("firedamage"))
                gameRules.setGameRule(GameRule.FREEZE_DAMAGE, d.getBoolean("freezedamage"))
                gameRules.setGameRule(GameRule.FUNCTION_COMMAND_LIMIT, d.getInt("functioncommandlimit"))
                gameRules.setGameRule(GameRule.KEEP_INVENTORY, d.getBoolean("keepinventory"))
                gameRules.setGameRule(GameRule.MAX_COMMAND_CHAIN_LENGTH, d.getInt("maxcommandchainlength"))
                gameRules.setGameRule(GameRule.MOB_GRIEFING, d.getBoolean("mobgriefing"))
                gameRules.setGameRule(GameRule.NATURAL_REGENERATION, d.getBoolean("naturalregeneration"))
                gameRules.setGameRule(GameRule.PVP, d.getBoolean("pvp"))
                gameRules.setGameRule(GameRule.RESPAWN_BLOCKS_EXPLODE, d.getBoolean("respawnblocksexplode"))
                gameRules.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, d.getBoolean("sendcommandfeedback"))
                gameRules.setGameRule(GameRule.SHOW_BORDER_EFFECT, d.getBoolean("showbordereffect"))
                gameRules.setGameRule(GameRule.SHOW_COORDINATES, d.getBoolean("showcoordinates"))
                gameRules.setGameRule(GameRule.SHOW_DEATH_MESSAGES, d.getBoolean("showdeathmessages"))
                gameRules.setGameRule(GameRule.SHOW_TAGS, d.getBoolean("showtags"))
                gameRules.setGameRule(GameRule.SPAWN_RADIUS, d.getInt("spawnradius"))
                gameRules.setGameRule(GameRule.TNT_EXPLODES, d.getBoolean("tntexplodes"))
                val levelDatBuilder = LevelDat.builder()
                    .biomeOverride(d.getString("BiomeOverride"))
                    .centerMapsToOrigin(d.getBoolean("CenterMapsToOrigin"))
                    .confirmedPlatformLockedContent(d.getBoolean("ConfirmedPlatformLockedContent"))
                    .difficulty(d.getInt("Difficulty"))
                    .flatWorldLayers(d.getString("FlatWorldLayers"))
                    .forceGameType(d.getBoolean("ForceGameType"))
                    .gameType(GameType.from(d.getInt("GameType")))
                    .generator(d.getInt("Generator"))
                    .inventoryVersion(d.getString("InventoryVersion"))
                    .LANBroadcast(d.getBoolean("LANBroadcast"))
                    .LANBroadcastIntent(d.getBoolean("LANBroadcastIntent"))
                    .lastPlayed(d.getLong("LastPlayed"))
                    .name(d.getString("LevelName"))
                    .limitedWorldOriginPoint(
                        BlockVector3(
                            d.getInt("LimitedWorldOriginX"),
                            d.getInt("LimitedWorldOriginY"),
                            d.getInt("LimitedWorldOriginZ")
                        )
                    )
                    .minimumCompatibleClientVersion(
                        SemVersion.from(
                            d.getList(
                                "MinimumCompatibleClientVersion",
                                IntTag::class.java
                            )
                        )
                    )
                    .multiplayerGame(d.getBoolean("MultiplayerGame"))
                    .multiplayerGameIntent(d.getBoolean("MultiplayerGameIntent"))
                    .netherScale(d.getInt("NetherScale"))
                    .networkVersion(d.getInt("NetworkVersion"))
                    .platform(d.getInt("Platform"))
                    .platformBroadcastIntent(d.getInt("PlatformBroadcastIntent"))
                    .randomSeed(d.getLong("RandomSeed"))
                    .spawnV1Villagers(d.getBoolean("SpawnV1Villagers"))
                    .spawnPoint(BlockVector3(d.getInt("SpawnX"), d.getInt("SpawnY"), d.getInt("SpawnZ")))
                    .storageVersion(d.getInt("StorageVersion"))
                    .time(d.getLong("Time"))
                    .worldVersion(d.getInt("WorldVersion"))
                    .XBLBroadcastIntent(d.getInt("XBLBroadcastIntent"))
                    .gameRules(gameRules)
                    .abilities(
                        Abilities.builder()
                            .attackMobs(abilities.getBoolean("attackmobs"))
                            .attackPlayers(abilities.getBoolean("attackplayers"))
                            .build(abilities.getBoolean("build"))
                            .doorsAndSwitches(abilities.getBoolean("doorsandswitches"))
                            .flySpeed(abilities.getFloat("flySpeed"))
                            .flying(abilities.getBoolean("flying"))
                            .instaBuild(abilities.getBoolean("instabuild"))
                            .invulnerable(abilities.getBoolean("invulnerable"))
                            .lightning(abilities.getBoolean("lightning"))
                            .mayFly(abilities.getBoolean("mayfly"))
                            .mine(abilities.getBoolean("mine"))
                            .op(abilities.getBoolean("op"))
                            .openContainers(abilities.getBoolean("opencontainers"))
                            .teleport(abilities.getBoolean("teleport"))
                            .walkSpeed(abilities.getFloat("walkSpeed"))
                            .build()
                    )
                    .baseGameVersion(d.getString("baseGameVersion"))
                    .bonusChestEnabled(d.getBoolean("bonusChestEnabled"))
                    .bonusChestSpawned(d.getBoolean("bonusChestSpawned"))
                    .cheatsEnabled(d.getBoolean("cheatsEnabled"))
                    .commandsEnabled(d.getBoolean("commandsEnabled"))
                    .currentTick(d.getLong("currentTick"))
                    .daylightCycle(d.getInt("daylightCycle"))
                    .editorWorldType(d.getInt("editorWorldType"))
                    .eduOffer(d.getInt("eduOffer"))
                    .educationFeaturesEnabled(d.getBoolean("educationFeaturesEnabled"))
                    .experiments(
                        Experiments.builder()
                            .cameras(experiments.getBoolean("cameras"))
                            .dataDrivenBiomes(experiments.getBoolean("data_driven_biomes"))
                            .dataDrivenItems(experiments.getBoolean("data_driven_items"))
                            .experimentalMolangFeatures(experiments.getBoolean("experimental_molang_features"))
                            .experimentsEverUsed(experiments.getBoolean("experiments_ever_used"))
                            .savedWithToggledExperiments(experiments.getBoolean("saved_with_toggled_experiments"))
                            .upcomingCreatorFeatures(experiments.getBoolean("upcoming_creator_features"))
                            .villagerTradesRebalance(experiments.getBoolean("villager_trades_rebalance"))
                            .build()
                    )
                    .hasBeenLoadedInCreative(d.getBoolean("hasBeenLoadedInCreative"))
                    .hasLockedBehaviorPack(d.getBoolean("hasLockedBehaviorPack"))
                    .hasLockedResourcePack(d.getBoolean("hasLockedResourcePack"))
                    .immutableWorld(d.getBoolean("immutableWorld"))
                    .isCreatedInEditor(d.getBoolean("isCreatedInEditor"))
                    .isExportedFromEditor(d.getBoolean("isExportedFromEditor"))
                    .isFromLockedTemplate(d.getBoolean("isFromLockedTemplate"))
                    .isFromWorldTemplate(d.getBoolean("isFromWorldTemplate"))
                    .isRandomSeedAllowed(d.getBoolean("isRandomSeedAllowed"))
                    .isSingleUseWorld(d.getBoolean("isSingleUseWorld"))
                    .isWorldTemplateOptionLocked(d.getBoolean("isWorldTemplateOptionLocked"))
                    .lastOpenedWithVersion(SemVersion.from(d.getList("lastOpenedWithVersion", IntTag::class.java)))
                    .lightningLevel(d.getFloat("lightningLevel"))
                    .lightningTime(d.getInt("lightningTime"))
                    .limitedWorldDepth(d.getInt("limitedWorldDepth"))
                    .limitedWorldWidth(d.getInt("limitedWorldWidth"))
                    .permissionsLevel(d.getInt("permissionsLevel"))
                    .playerPermissionsLevel(d.getInt("playerPermissionsLevel"))
                    .playersSleepingPercentage(d.getInt("playerssleepingpercentage"))
                    .prid(d.getString("prid"))
                    .rainLevel(d.getFloat("rainLevel"))
                    .rainTime(d.getInt("rainTime"))
                    .randomTickSpeed(d.getInt("randomtickspeed"))
                    .recipesUnlock(d.getBoolean("recipesunlock"))
                    .requiresCopiedPackRemovalCheck(d.getBoolean("requiresCopiedPackRemovalCheck"))
                    .serverChunkTickRange(d.getInt("serverChunkTickRange"))
                    .spawnMobs(d.getBoolean("spawnMobs"))
                    .startWithMapEnabled(d.getBoolean("startWithMapEnabled"))
                    .texturePacksRequired(d.getBoolean("texturePacksRequired"))
                    .useMsaGamertagsOnly(d.getBoolean("useMsaGamertagsOnly"))
                    .worldStartCount(d.getLong("worldStartCount"))
                    .worldPolicies(WorldPolicies.builder().build())
                if (d.contains("raining")) {
                    levelDatBuilder.raining(d.getBoolean("raining")) //PNX Custom field
                }
                if (d.contains("thundering")) {
                    levelDatBuilder.thundering(d.getBoolean("thundering")) //PNX Custom field
                }
                return levelDatBuilder.build()
            }
        } catch (e: FileNotFoundException) {
            LevelDBProvider.log.error("The level.dat file does not exist!")
        }
        throw RuntimeException("level.dat is null!")
    }

    companion object {
        val CACHE: HashMap<String, LevelDBStorage> = HashMap()
        private val levelDatMagic = byteArrayOf(10, 0, 0, 0, 68, 11, 0, 0)

        @UsedByReflection
        @Throws(IOException::class)
        fun generate(path: String, name: String?, generatorConfig: GeneratorConfig) {
            val dataDir = File("$path/db")
            if (!dataDir.exists() && !dataDir.mkdirs()) {
                throw IOException("Could not create the directory $dataDir")
            }
            val levelData = LevelDat.builder()
                .randomSeed(generatorConfig.seed())
                .name(name)
                .lastPlayed(System.currentTimeMillis() / 1000)
                .build()
            writeLevelDat(path, generatorConfig.dimensionData(), levelData)
        }

        @UsedByReflection
        fun isValid(path: String?): Boolean {
            val isValid = (File(path, "level.dat").exists()) && File(path, "db").isDirectory
            if (isValid) {
                for (file in Objects.requireNonNull(File(path, "db").listFiles())) {
                    if (file.name.endsWith(".ldb")) {
                        return true
                    }
                }
            }
            return false
        }

        fun writeLevelDat(pathName: String, dimensionData: DimensionData, levelDat: LevelDat) {
            val path = Path.of(pathName)
            var levelDatName = "level.dat"
            if (dimensionData.dimensionId != 0) {
                levelDatName = "level_Dim%s.dat".formatted(dimensionData.dimensionId)
            }
            val levelDatNow = path.resolve(levelDatName).toFile()
            try {
                FileOutputStream(levelDatNow).use { output ->
                    if (levelDatNow.exists()) {
                        Files.copy(
                            path.resolve(levelDatName),
                            path.resolve(levelDatName + "_old"),
                            StandardCopyOption.REPLACE_EXISTING
                        )
                    } else {
                        levelDatNow.createNewFile()
                    }
                    output.write(levelDatMagic) //magic number
                    NBTIO.write(createWorldDataNBT(levelDat), output, ByteOrder.LITTLE_ENDIAN)
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }

        private fun createWorldDataNBT(worldData: LevelDat): CompoundTag {
            val levelDat = CompoundTag()

            levelDat.putString("BiomeOverride", worldData.getBiomeOverride())
            levelDat.putBoolean("CenterMapsToOrigin", worldData.isCenterMapsToOrigin)
            levelDat.putBoolean("ConfirmedPlatformLockedContent", worldData.isConfirmedPlatformLockedContent)
            levelDat.putInt("Difficulty", worldData.getDifficulty())
            levelDat.putString("FlatWorldLayers", worldData.getFlatWorldLayers())
            levelDat.putBoolean("ForceGameType", worldData.isForceGameType)
            levelDat.putInt("GameType", worldData.getGameType().ordinal)
            levelDat.putInt("Generator", worldData.getGenerator())
            levelDat.putString("InventoryVersion", worldData.getInventoryVersion())
            levelDat.putBoolean("LANBroadcast", worldData.isLANBroadcast)
            levelDat.putBoolean("LANBroadcastIntent", worldData.isLANBroadcastIntent)
            levelDat.putLong("LastPlayed", worldData.getLastPlayed())
            levelDat.putString("LevelName", worldData.getName())
            levelDat.putInt("LimitedWorldOriginX", worldData.getLimitedWorldOriginPoint().getX())
            levelDat.putInt("LimitedWorldOriginY", worldData.getLimitedWorldOriginPoint().getY())
            levelDat.putInt("LimitedWorldOriginZ", worldData.getLimitedWorldOriginPoint().getZ())
            levelDat.putList("MinimumCompatibleClientVersion", worldData.getMinimumCompatibleClientVersion().toTag())
            levelDat.putList("lastOpenedWithVersion", worldData.getLastOpenedWithVersion().toTag())
            levelDat.putBoolean("MultiplayerGame", worldData.isMultiplayerGame)
            levelDat.putBoolean("MultiplayerGameIntent", worldData.isMultiplayerGameIntent)
            levelDat.putInt("NetherScale", worldData.getNetherScale())
            levelDat.putInt("NetworkVersion", worldData.getNetworkVersion())
            levelDat.putInt("Platform", worldData.getPlatform())
            levelDat.putInt("PlatformBroadcastIntent", worldData.getPlatformBroadcastIntent())
            levelDat.putLong("RandomSeed", worldData.getRandomSeed())
            levelDat.putBoolean("SpawnV1Villagers", worldData.isSpawnV1Villagers)
            levelDat.putInt("SpawnX", worldData.getSpawnPoint().getX())
            levelDat.putInt("SpawnY", worldData.getSpawnPoint().getY())
            levelDat.putInt("SpawnZ", worldData.getSpawnPoint().getZ())
            levelDat.putInt("StorageVersion", worldData.getStorageVersion())
            levelDat.putLong("Time", worldData.getTime())
            levelDat.putInt("WorldVersion", worldData.getWorldVersion())
            levelDat.putInt("XBLBroadcastIntent", worldData.xblBroadcastIntent)
            val abilities = CompoundTag()
                .putBoolean("attackmobs", worldData.getAbilities().isAttackMobs)
                .putBoolean("attackplayers", worldData.getAbilities().isAttackPlayers)
                .putBoolean("build", worldData.getAbilities().isBuild)
                .putBoolean("doorsandswitches", worldData.getAbilities().isDoorsAndSwitches)
                .putBoolean("flying", worldData.getAbilities().isFlying)
                .putBoolean("instabuild", worldData.getAbilities().isInstaBuild)
                .putBoolean("invulnerable", worldData.getAbilities().isInvulnerable)
                .putBoolean("lightning", worldData.getAbilities().isLightning)
                .putBoolean("mayfly", worldData.getAbilities().isMayFly)
                .putBoolean("mine", worldData.getAbilities().isMine)
                .putBoolean("op", worldData.getAbilities().isOp)
                .putBoolean("opencontainers", worldData.getAbilities().isOpenContainers)
                .putBoolean("teleport", worldData.getAbilities().isTeleport)
                .putFloat("flySpeed", worldData.getAbilities().flySpeed)
                .putFloat("walkSpeed", worldData.getAbilities().walkSpeed)
            val experiments = CompoundTag()
                .putBoolean("cameras", worldData.getExperiments().isCameras)
                .putBoolean("data_driven_biomes", worldData.getExperiments().isDataDrivenBiomes)
                .putBoolean("data_driven_items", worldData.getExperiments().isDataDrivenItems)
                .putBoolean("experimental_molang_features", worldData.getExperiments().isExperimentalMolangFeatures)
                .putBoolean("experiments_ever_used", worldData.getExperiments().isExperimentsEverUsed)
                .putBoolean("gametest", worldData.getExperiments().isGametest)
                .putBoolean("saved_with_toggled_experiments", worldData.getExperiments().isSavedWithToggledExperiments)
                .putBoolean("upcoming_creator_features", worldData.getExperiments().isUpcomingCreatorFeatures)
                .putBoolean("villager_trades_rebalance", worldData.getExperiments().isVillagerTradesRebalance)
            levelDat.put("abilities", abilities)
            levelDat.put("experiments", experiments)

            levelDat.putBoolean("bonusChestEnabled", worldData.isBonusChestEnabled)
            levelDat.putBoolean("bonusChestSpawned", worldData.isBonusChestSpawned)
            levelDat.putBoolean("cheatsEnabled", worldData.isCheatsEnabled)
            levelDat.putBoolean("commandsEnabled", worldData.isCommandsEnabled)
            levelDat.putLong("currentTick", worldData.getCurrentTick())
            levelDat.putInt("daylightCycle", worldData.getDaylightCycle())
            levelDat.putInt("editorWorldType", worldData.getEditorWorldType())
            levelDat.putInt("eduOffer", worldData.getEduOffer())
            levelDat.putBoolean("educationFeaturesEnabled", worldData.isEducationFeaturesEnabled)

            levelDat.put(
                "commandblockoutput",
                worldData.getGameRules().gameRules[GameRule.COMMAND_BLOCK_OUTPUT].getTag()
            )
            levelDat.put(
                "commandblocksenabled",
                worldData.getGameRules().gameRules[GameRule.COMMAND_BLOCKS_ENABLED].getTag()
            )
            levelDat.put("dodaylightcycle", worldData.getGameRules().gameRules[GameRule.DO_DAYLIGHT_CYCLE].getTag())
            levelDat.put("doentitydrops", worldData.getGameRules().gameRules[GameRule.DO_ENTITY_DROPS].getTag())
            levelDat.put("dofiretick", worldData.getGameRules().gameRules[GameRule.DO_FIRE_TICK].getTag())
            levelDat.put(
                "doimmediaterespawn",
                worldData.getGameRules().gameRules[GameRule.DO_IMMEDIATE_RESPAWN].getTag()
            )
            levelDat.put("doinsomnia", worldData.getGameRules().gameRules[GameRule.DO_INSOMNIA].getTag())
            levelDat.put("dolimitedcrafting", worldData.getGameRules().gameRules[GameRule.DO_LIMITED_CRAFTING].getTag())
            levelDat.put("domobloot", worldData.getGameRules().gameRules[GameRule.DO_MOB_LOOT].getTag())
            levelDat.put("domobspawning", worldData.getGameRules().gameRules[GameRule.DO_MOB_SPAWNING].getTag())
            levelDat.put("dotiledrops", worldData.getGameRules().gameRules[GameRule.DO_TILE_DROPS].getTag())
            levelDat.put("doweathercycle", worldData.getGameRules().gameRules[GameRule.DO_WEATHER_CYCLE].getTag())
            levelDat.put("drowningdamage", worldData.getGameRules().gameRules[GameRule.DROWNING_DAMAGE].getTag())
            levelDat.put("falldamage", worldData.getGameRules().gameRules[GameRule.FALL_DAMAGE].getTag())
            levelDat.put("firedamage", worldData.getGameRules().gameRules[GameRule.FIRE_DAMAGE].getTag())
            levelDat.put("freezedamage", worldData.getGameRules().gameRules[GameRule.FREEZE_DAMAGE].getTag())
            levelDat.put(
                "functioncommandlimit",
                worldData.getGameRules().gameRules[GameRule.FUNCTION_COMMAND_LIMIT].getTag()
            )
            levelDat.put("keepinventory", worldData.getGameRules().gameRules[GameRule.KEEP_INVENTORY].getTag())
            levelDat.put(
                "maxcommandchainlength",
                worldData.getGameRules().gameRules[GameRule.MAX_COMMAND_CHAIN_LENGTH].getTag()
            )
            levelDat.put("mobgriefing", worldData.getGameRules().gameRules[GameRule.MOB_GRIEFING].getTag())
            levelDat.put(
                "naturalregeneration",
                worldData.getGameRules().gameRules[GameRule.NATURAL_REGENERATION].getTag()
            )
            levelDat.put("pvp", worldData.getGameRules().gameRules[GameRule.PVP].getTag())
            levelDat.put(
                "respawnblocksexplode",
                worldData.getGameRules().gameRules[GameRule.RESPAWN_BLOCKS_EXPLODE].getTag()
            )
            levelDat.put(
                "sendcommandfeedback",
                worldData.getGameRules().gameRules[GameRule.SEND_COMMAND_FEEDBACK].getTag()
            )
            levelDat.put("showbordereffect", worldData.getGameRules().gameRules[GameRule.SHOW_BORDER_EFFECT].getTag())
            levelDat.put("showcoordinates", worldData.getGameRules().gameRules[GameRule.SHOW_COORDINATES].getTag())
            levelDat.put("showdeathmessages", worldData.getGameRules().gameRules[GameRule.SHOW_DEATH_MESSAGES].getTag())
            levelDat.put("showtags", worldData.getGameRules().gameRules[GameRule.SHOW_TAGS].getTag())
            levelDat.put("spawnradius", worldData.getGameRules().gameRules[GameRule.SPAWN_RADIUS].getTag())
            levelDat.put("tntexplodes", worldData.getGameRules().gameRules[GameRule.TNT_EXPLODES].getTag())

            //PNX Custom field
            levelDat.putBoolean("raining", worldData.isRaining)
            levelDat.putBoolean("thundering", worldData.isThundering)
            return levelDat
        }
    }
}
