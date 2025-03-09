package org.chorus.level.generator.terra.handles

import cn.nukkit.block.BlockAir
import cn.nukkit.level.generator.terra.PNXAdapter
import cn.nukkit.level.generator.terra.delegate.PNXBlockStateDelegate
import cn.nukkit.level.generator.terra.delegate.PNXEntityType
import cn.nukkit.level.generator.terra.mappings.JeBlockState
import cn.nukkit.level.generator.terra.mappings.MappingRegistries
import com.dfsek.terra.api.block.state.BlockState
import com.dfsek.terra.api.entity.EntityType
import com.dfsek.terra.api.handle.WorldHandle
import lombok.extern.slf4j.Slf4j

@Slf4j
class PNXWorldHandle : WorldHandle {
    override fun createBlockState(s: String): BlockState {
        var s = s
        if (!s.startsWith("minecraft:")) {
            s = "minecraft:$s"
        }
        //修正部分属性缺失以能正确获取对应基岩版映射
        when (s) {
            "minecraft:water" -> s = "minecraft:water[level=0]"
            "minecraft:lava" -> s = "minecraft:lava[level=0]"
            "minecraft:deepslate" -> s = "minecraft:deepslate[axis=y]"
            "minecraft:grass_block" -> s = "minecraft:grass_block[snowy=false]"
            "minecraft:podzol" -> s = "minecraft:podzol[snowy=false]"
            "minecraft:mycelium" -> s = "minecraft:mycelium[snowy=false]"
            "minecraft:sugar_cane" -> s = "minecraft:sugar_cane[age=0]"
            "minecraft:brown_mushroom_block[down=false]" -> s =
                "minecraft:brown_mushroom_block[down=false,east=true,north=true,south=true,up=true,west=true]"

            "minecraft:cactus" -> s = "minecraft:cactus[age=0]"
            "minecraft:mushroom_stem" -> s =
                "minecraft:mushroom_stem[down=true,east=true,north=true,south=true,up=true,west=true]"

            "minecraft:jungle_wood" -> s = "minecraft:jungle_wood[axis=y]"
            "minecraft:redstone_ore" -> s = "minecraft:redstone_ore[lit=false]"
            "minecraft:deepslate_redstone_ore" -> s = "minecraft:deepslate_redstone_ore[lit=false]"
            "minecraft:basalt" -> s = "minecraft:basalt[axis=y]"
            "minecraft:snow" -> s = "minecraft:snow[layers=1]"
            "minecraft:cave_vines" -> s = "minecraft:cave_vines[age=0,berries=true]"
            "minecraft:polished_basalt" -> s = "minecraft:polished_basalt[axis=y]"
            "minecraft:azalea_leaves[persistent=true]" -> s =
                "minecraft:azalea_leaves[distance=1,persistent=true,waterlogged=false]"

            "minecraft:flowering_azalea_leaves[persistent=true]" -> s =
                "minecraft:flowering_azalea_leaves[distance=1,persistent=true,waterlogged=false]"

            "minecraft:deepslate_tile_wall" -> s =
                "minecraft:deepslate_tile_wall[east=none,north=none,south=none,up=true,waterlogged=false,west=none]"

            "minecraft:farmland" -> s = "minecraft:farmland[moisture=7]"
            "minecraft:cobbled_deepslate_wall" -> s =
                "minecraft:cobbled_deepslate_wall[east=none,north=none,south=none,up=true,waterlogged=false,west=none]"

            "minecraft:polished_deepslate_wall" -> s =
                "minecraft:polished_deepslate_wall[east=none,north=none,south=none,up=true,waterlogged=false,west=none]"

            "minecraft:sculk_shrieker[can_summon=true]" -> s =
                "minecraft:sculk_shrieker[can_summon=true,shrieking=false,waterlogged=false]"

            "minecraft:sculk_catalyst" -> s = "minecraft:sculk_catalyst[bloom=false]"
            "minecraft:dark_oak_fence" -> s =
                "minecraft:dark_oak_fence[east=false,north=false,south=false,waterlogged=false,west=false]"

            "minecraft:sculk_sensor" -> s =
                "minecraft:sculk_sensor[power=0,sculk_sensor_phase=inactive,waterlogged=false]"

            "minecraft:deepslate_tile_stairs" -> s =
                "minecraft:deepslate_tile_stairs[facing=north,half=top,shape=straight,waterlogged=false]"
        }
        val jeBlockState = JeBlockState(s)
        val jeBlockIdentifier = jeBlockState.identifier
        val jeBlockAttributes = jeBlockState.attributes
        if (jeBlockIdentifier.contains("log") || jeBlockIdentifier.contains("wood")) {
            jeBlockAttributes.putIfAbsent("axis", "y")
        }
        if (jeBlockIdentifier.contains("minecraft:leaves")) {
            jeBlockAttributes.putIfAbsent("distance", "7")
            jeBlockAttributes.putIfAbsent("persistent", "true")
        }
        if (jeBlockIdentifier == "minecraft:bee_nest") jeBlockAttributes.putIfAbsent("honey_level", "0")
        if (jeBlockIdentifier == "minecraft:vine") {
            jeBlockAttributes.putIfAbsent("east", "false")
            jeBlockAttributes.putIfAbsent("north", "false")
            jeBlockAttributes.putIfAbsent("south", "false")
            jeBlockAttributes.putIfAbsent("up", "false")
            jeBlockAttributes.putIfAbsent("west", "false")
        }
        var bedrockBlockState = MappingRegistries.BLOCKS!!.getPNXBlock(jeBlockState)
        //若未获取到属性，排除掉含水再次尝试
        if (bedrockBlockState == null) {
            jeBlockState.isEqualsIgnoreWaterlogged = true
            bedrockBlockState = MappingRegistries.BLOCKS.getPNXBlock(jeBlockState)
        }
        //排除所有属性再次尝试
        if (bedrockBlockState == null) {
            jeBlockState.isEqualsIgnoreAttributes = true
            bedrockBlockState = MappingRegistries.BLOCKS.getPNXBlock(jeBlockState)
        }
        if (bedrockBlockState == null) {
            PNXWorldHandle.log.error("[Terra] Can't find block mapping for$jeBlockState")
            return AIR
        }
        return PNXAdapter.adapt(bedrockBlockState)
    }

    override fun air(): BlockState {
        return AIR
    }

    override fun getEntity(s: String): EntityType {
        val entityType = PNXEntityType(s)
        requireNotNull(entityType.handle) { "Unknown entity type!" }
        return entityType
    }

    companion object {
        val AIR: PNXBlockStateDelegate = PNXBlockStateDelegate(BlockAir.STATE)
    }
}
