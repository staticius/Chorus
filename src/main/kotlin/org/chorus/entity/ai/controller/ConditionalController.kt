package org.chorus.entity.ai.controller

import it.unimi.dsi.fastutil.Pair
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import org.chorus.entity.mob.EntityMob
import java.util.*
import java.util.function.Predicate

class ConditionalController(vararg controllers: Pair<Predicate<EntityMob>, IController>) :
    IController {
    private val controllers = Object2ObjectArrayMap<Predicate<EntityMob>, IController>()

    init {
        Arrays.stream(controllers).forEach { pair: Pair<Predicate<EntityMob>, IController> ->
            this.controllers[pair.first()] =
                pair.second()
        }
    }

    override fun control(entity: EntityMob): Boolean {
        var successful = false
        for ((key, value) in controllers.object2ObjectEntrySet()) {
            if (key.test(entity)) {
                if (value.control(entity)) {
                    successful = true
                }
            }
        }
        return successful
    }
}
