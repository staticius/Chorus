package cn.nukkit.event.potion

import cn.nukkit.entity.effect.PotionType
import cn.nukkit.event.Event

/**
 * @author Snake1999
 * @since 2016/1/12
 */
abstract class PotionEvent(var potion: PotionType) : Event()
