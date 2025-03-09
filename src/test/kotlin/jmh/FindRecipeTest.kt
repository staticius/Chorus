package jmh

import org.chorus.block.BlockID
import org.chorus.item.Item
import org.chorus.item.ItemID
import org.chorus.registry.Registries
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import org.openjdk.jmh.runner.Runner
import org.openjdk.jmh.runner.RunnerException
import org.openjdk.jmh.runner.options.OptionsBuilder
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Threads(1)
@Fork(1)
class FindRecipeTest {
    @Setup
    fun setup() {
        Registries.POTION.init()
        Registries.BLOCKSTATE_ITEMMETA.init()
        Registries.BLOCK.init()
        Registries.ITEM.init()
        Registries.ITEM_RUNTIMEID.init()
        Registries.RECIPE.init()
    }

    @Benchmark
    fun test_findBlastFurnaceRecipe_fail(hole: Blackhole) { //46
        hole.consume(Registries.RECIPE.findBlastFurnaceRecipe(Item.get(ItemID.IRON_NUGGET)))
    }

    @Benchmark
    fun test_findBlastFurnaceRecipe_success(hole: Blackhole) { //46
        hole.consume(Registries.RECIPE.findBlastFurnaceRecipe(Item.get(ItemID.IRON_PICKAXE)))
    }

    @Benchmark
    fun test_findShapelessRecipe(hole: Blackhole) { //1135
        hole.consume(
            Registries.RECIPE.findShapelessRecipe(
                Item.get(BlockID.BLUE_SHULKER_BOX),
                Item.get(ItemID.BROWN_DYE)
            )
        )
    }

    companion object {
        @Throws(RunnerException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val opt = OptionsBuilder()
                .include(FindRecipeTest::class.java.simpleName)
                .build()
            Runner(opt).run()
        }
    }
}
