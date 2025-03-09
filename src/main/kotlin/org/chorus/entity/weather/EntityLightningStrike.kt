package org.chorus.entity.weather

/**
 * @author funcraft
 * @since 2016/2/27
 */
interface EntityLightningStrike : EntityWeather {
    fun isEffect(): Boolean

    fun setEffect(e: Boolean)
}
