package org.chorus.entity.weather

interface EntityLightningStrike : EntityWeather {
    fun isEffect(): Boolean

    fun setEffect(e: Boolean)
}
