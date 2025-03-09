package org.chorus.level

import cn.nukkit.Player
import cn.nukkit.block.*
import cn.nukkit.math.Vector3
import cn.nukkit.registry.Registries
import it.unimi.dsi.fastutil.ints.*
import java.util.List

class AntiXraySystem(private val level: Level) {
    var fakeOreDenominator: Int = 16
    var isPreDeObfuscate: Boolean = true
    val rawRealOreToReplacedRuntimeIdMap: Int2IntMap = Int2IntOpenHashMap(24)
    private val fakeOreToPutRuntimeIds = Int2ObjectOpenHashMap<IntList>(4)

    fun addAntiXrayOreBlock(oreBlock: Block, replaceWith: Block) {
        rawRealOreToReplacedRuntimeIdMap.put(oreBlock.runtimeId, replaceWith.runtimeId)
    }

    fun removeAntiXrayOreBlock(oreBlock: Block, replaceWith: Block) {
        rawRealOreToReplacedRuntimeIdMap.remove(oreBlock.runtimeId, replaceWith.runtimeId)
    }

    fun addAntiXrayFakeBlock(originBlock: Block, fakeBlocks: Collection<Block>) {
        val rid = originBlock.runtimeId
        var list = fakeOreToPutRuntimeIds[rid]
        if (list == null) {
            fakeOreToPutRuntimeIds.put(rid, IntArrayList(8).also { list = it })
        }
        for (each in fakeBlocks) {
            list!!.add(each.runtimeId)
        }
    }

    fun removeAntiXrayFakeBlock(originBlock: Block, fakeBlocks: Collection<Block>) {
        val rid = originBlock.runtimeId
        val list = fakeOreToPutRuntimeIds[rid]
        if (list != null) {
            for (each in fakeBlocks) {
                val tmp = each.runtimeId
                list.removeIf(IntPredicate { i: Int -> i == tmp })
            }
        }
    }

    val rawFakeOreToPutRuntimeIdMap: Int2ObjectMap<IntList>
        get() = this.fakeOreToPutRuntimeIds

    fun obfuscateSendBlocks(index: Long, playerArray: Array<Player>, blocks: Int2ObjectOpenHashMap<Any?>) {
        val size: Int = blocks.size()
        val vectorSet = IntOpenHashSet(size * 6)
        val vRidList = ArrayList<Vector3WithRuntimeId>(size * 7)
        var tmpV3Rid: Vector3WithRuntimeId
        for (blockHash in blocks.keySet()) {
            var blockHash = blockHash
            val hash: Vector3 = Level.Companion.getBlockXYZ(index, blockHash, level)
            var x = hash.floorX
            var y = hash.floorY
            var z = hash.floorZ
            if (!vectorSet.contains(blockHash)) {
                vectorSet.add(blockHash)
                try {
                    tmpV3Rid = Vector3WithRuntimeId(
                        x.toDouble(),
                        y.toDouble(),
                        z.toDouble(),
                        level.getBlockRuntimeId(x, y, z, 0),
                        level.getBlockRuntimeId(x, y, z, 1)
                    )
                    vRidList.add(tmpV3Rid)
                    if (!transparentBlockRuntimeIds.contains(tmpV3Rid.runtimeIdLayer0)) {
                        continue
                    }
                } catch (ignore: Exception) {
                }
            }
            x++
            blockHash = Level.Companion.localBlockHash(x, y, z, 0, level)
            if (!vectorSet.contains(blockHash)) {
                vectorSet.add(blockHash)
                try {
                    vRidList.add(
                        Vector3WithRuntimeId(
                            x.toDouble(),
                            y.toDouble(),
                            z.toDouble(),
                            level.getBlockRuntimeId(x, y, z, 0),
                            level.getBlockRuntimeId(x, y, z, 1)
                        )
                    )
                } catch (ignore: Exception) {
                }
            }
            x -= 2
            blockHash = Level.Companion.localBlockHash(x, y, z, 0, level)
            if (!vectorSet.contains(blockHash)) {
                vectorSet.add(blockHash)
                try {
                    vRidList.add(
                        Vector3WithRuntimeId(
                            x.toDouble(),
                            y.toDouble(),
                            z.toDouble(),
                            level.getBlockRuntimeId(x, y, z, 0),
                            level.getBlockRuntimeId(x, y, z, 1)
                        )
                    )
                } catch (ignore: Exception) {
                }
            }
            x++
            y++
            blockHash = Level.Companion.localBlockHash(x, y, z, 0, level)
            if (!vectorSet.contains(blockHash)) {
                vectorSet.add(blockHash)
                try {
                    vRidList.add(
                        Vector3WithRuntimeId(
                            x.toDouble(),
                            y.toDouble(),
                            z.toDouble(),
                            level.getBlockRuntimeId(x, y, z, 0),
                            level.getBlockRuntimeId(x, y, z, 1)
                        )
                    )
                } catch (ignore: Exception) {
                }
            }
            y -= 2
            blockHash = Level.Companion.localBlockHash(x, y, z, 0, level)
            if (!vectorSet.contains(blockHash)) {
                vectorSet.add(blockHash)
                try {
                    vRidList.add(
                        Vector3WithRuntimeId(
                            x.toDouble(),
                            y.toDouble(),
                            z.toDouble(),
                            level.getBlockRuntimeId(x, y, z, 0),
                            level.getBlockRuntimeId(x, y, z, 1)
                        )
                    )
                } catch (ignore: Exception) {
                }
            }
            y++
            z++
            blockHash = Level.Companion.localBlockHash(x, y, z, 0, level)
            if (!vectorSet.contains(blockHash)) {
                vectorSet.add(blockHash)
                try {
                    vRidList.add(
                        Vector3WithRuntimeId(
                            x.toDouble(),
                            y.toDouble(),
                            z.toDouble(),
                            level.getBlockRuntimeId(x, y, z, 0),
                            level.getBlockRuntimeId(x, y, z, 1)
                        )
                    )
                } catch (ignore: Exception) {
                }
            }
            z -= 2
            blockHash = Level.Companion.localBlockHash(x, y, z, 0, level)
            if (!vectorSet.contains(blockHash)) {
                vectorSet.add(blockHash)
                try {
                    vRidList.add(
                        Vector3WithRuntimeId(
                            x.toDouble(),
                            y.toDouble(),
                            z.toDouble(),
                            level.getBlockRuntimeId(x, y, z, 0),
                            level.getBlockRuntimeId(x, y, z, 1)
                        )
                    )
                } catch (ignore: Exception) {
                }
            }
        }
        level.sendBlocks(playerArray, vRidList.toArray<Vector3?> { _Dummy_.__Array__() }, UpdateBlockPacket.FLAG_ALL)
    }

    fun deObfuscateBlock(player: Player, face: BlockFace, target: Block) {
        val vecList = ArrayList<Vector3WithRuntimeId>(5)
        var tmpVec: Vector3WithRuntimeId
        for (each in BlockFace.entries) {
            if (each == face) continue
            val tmpX: Int = target.position.floorX + each.getXOffset()
            val tmpY: Int = target.position.floorY + each.getYOffset()
            val tmpZ: Int = target.position.floorZ + each.getZOffset()
            try {
                tmpVec = Vector3WithRuntimeId(
                    tmpX.toDouble(),
                    tmpY.toDouble(),
                    tmpZ.toDouble(),
                    level.getBlockRuntimeId(tmpX, tmpY, tmpZ, 0),
                    level.getBlockRuntimeId(tmpX, tmpY, tmpZ, 1)
                )
                if (rawFakeOreToPutRuntimeIdMap.containsKey(tmpVec.runtimeIdLayer0)) {
                    vecList.add(tmpVec)
                }
            } catch (ignore: Exception) {
            }
        }
        level.sendBlocks(
            arrayOf<Player>(player),
            vecList.toArray<Vector3?> { _Dummy_.__Array__() },
            UpdateBlockPacket.FLAG_ALL
        )
    }

    fun reinitAntiXray(global: Boolean) {
        val stone: BlockStone = BlockStone()
        val netherRack: BlockNetherrack = BlockNetherrack()
        val deepSlate: BlockDeepslate = BlockDeepslate()
        run {
            rawFakeOreToPutRuntimeIdMap.clear()
            rawRealOreToReplacedRuntimeIdMap.clear()
        }
        run {
            addAntiXrayOreBlock(BlockCoalOre(), stone)
            addAntiXrayOreBlock(BlockDiamondOre(), stone)
            addAntiXrayOreBlock(BlockEmeraldOre(), stone)
            addAntiXrayOreBlock(BlockGoldOre(), stone)
            addAntiXrayOreBlock(BlockIronOre(), stone)
            addAntiXrayOreBlock(BlockLapisOre(), stone)
            addAntiXrayOreBlock(BlockRedstoneOre(), stone)
            addAntiXrayOreBlock(BlockQuartzOre(), netherRack)
            addAntiXrayOreBlock(BlockNetherGoldOre(), netherRack)
            addAntiXrayOreBlock(BlockAncientDebris(), netherRack)
            addAntiXrayOreBlock(BlockDeepslateCoalOre(), deepSlate)
            addAntiXrayOreBlock(BlockDeepslateDiamondOre(), deepSlate)
            addAntiXrayOreBlock(BlockDeepslateEmeraldOre(), deepSlate)
            addAntiXrayOreBlock(BlockDeepslateGoldOre(), deepSlate)
            addAntiXrayOreBlock(BlockDeepslateIronOre(), deepSlate)
            addAntiXrayOreBlock(BlockDeepslateLapisOre(), deepSlate)
            addAntiXrayOreBlock(BlockDeepslateRedstoneOre(), deepSlate)
        }
        run {
            addAntiXrayFakeBlock(
                stone,
                List.of<Block>(
                    BlockCoalOre(),
                    BlockDiamondOre(),
                    BlockEmeraldOre(),
                    BlockGoldOre(),
                    BlockIronOre(),
                    BlockLapisOre(),
                    BlockRedstoneOre()
                )
            )
            addAntiXrayFakeBlock(
                netherRack,
                List.of<Block>(BlockQuartzOre(), BlockNetherGoldOre(), BlockAncientDebris())
            )
            addAntiXrayFakeBlock(
                deepSlate,
                List.of<Block>(
                    BlockDeepslateCoalOre(),
                    BlockDeepslateDiamondOre(),
                    BlockDeepslateEmeraldOre(),
                    BlockDeepslateGoldOre(),
                    BlockDeepslateIronOre(),
                    BlockDeepslateLapisOre(),
                    BlockDeepslateRedstoneOre()
                )
            )
        }
        if (global || transparentBlockRuntimeIds.isEmpty()) {
            transparentBlockRuntimeIds.clear()
            for (each in Registries.BLOCKSTATE.allState) {
                try {
                    val block = Block.get(each)
                    if (block.isTransparent) {
                        transparentBlockRuntimeIds.add(block.runtimeId)
                    }
                } catch (ignore: Exception) {
                }
            }
            transparentBlockRuntimeIds.trim()
        }
    }

    companion object {
        private val transparentBlockRuntimeIds = IntOpenHashSet(256)
        val rawTransparentBlockRuntimeIds: IntSet
            get() = transparentBlockRuntimeIds
    }
}
