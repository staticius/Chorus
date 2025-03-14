package org.chorus.metadata

import com.dfsek.terra.lib.commons.lang3.Validate
import org.chorus.metadata.LazyMetadataValue.CacheStrategy
import org.chorus.plugin.Plugin
import java.lang.ref.SoftReference
import java.util.concurrent.Callable

/**
 * The LazyMetadataValue class implements a type of metadata that is not
 * computed until another plugin asks for it.
 *
 *
 * By making metadata values lazy, no computation is done by the providing
 * plugin until absolutely necessary (if ever). Additionally,
 * LazyMetadataValue objects cache their values internally unless overridden
 * by a [CacheStrategy] or invalidated at the individual or plugin
 * level. Once invalidated, the LazyMetadataValue will recompute its value
 * when asked.
 */
open class LazyMetadataValue : MetadataValueAdapter {
    private var lazyValue: Callable<Any?>? = null
    private var cacheStrategy: CacheStrategy? = null
    private var internalValue: SoftReference<Any?>? = null

    /**
     * Initialized a LazyMetadataValue object with the default
     * CACHE_AFTER_FIRST_EVAL cache strategy.
     *
     * @param owningPlugin the [Plugin] that created this metadata
     * value.
     * @param lazyValue    the lazy value assigned to this metadata value.
     */
    constructor(owningPlugin: Plugin, lazyValue: Callable<Any?>) : this(
        owningPlugin,
        CacheStrategy.CACHE_AFTER_FIRST_EVAL,
        lazyValue
    )

    /**
     * Initializes a LazyMetadataValue object with a specific cache strategy.
     *
     * @param owningPlugin  the [Plugin] that created this metadata
     * value.
     * @param cacheStrategy determines the rules for caching this metadata
     * value.
     * @param lazyValue     the lazy value assigned to this metadata value.
     */
    constructor(owningPlugin: Plugin, cacheStrategy: CacheStrategy, lazyValue: Callable<Any?>) : super(owningPlugin) {
        Validate.notNull(cacheStrategy, "cacheStrategy cannot be null")
        Validate.notNull(lazyValue, "lazyValue cannot be null")
        this.internalValue = SoftReference(null)
        this.lazyValue = lazyValue
        this.cacheStrategy = cacheStrategy
    }

    /**
     * Protected special constructor used by FixedMetadataValue to bypass
     * standard setup.
     */
    protected constructor(owningPlugin: Plugin) : super(owningPlugin)

    override fun value(): Any? {
        eval()
        val value = internalValue!!.get()
        if (value === ACTUALLY_NULL) {
            return null
        }
        return value
    }

    /**
     * Lazily evaluates the value of this metadata item.
     *
     * @throws MetadataEvaluationException if computing the metadata value
     * fails.
     */
    @Synchronized
    @Throws(MetadataEvaluationException::class)
    private fun eval() {
        if (cacheStrategy == CacheStrategy.NEVER_CACHE || internalValue!!.get() == null) {
            try {
                var value = lazyValue!!.call()
                if (value == null) {
                    value = ACTUALLY_NULL
                }
                internalValue = SoftReference(value)
            } catch (e: Exception) {
                throw MetadataEvaluationException(e)
            }
        }
    }

    @Synchronized
    override fun invalidate() {
        if (cacheStrategy != CacheStrategy.CACHE_ETERNALLY) {
            internalValue!!.clear()
        }
    }

    /**
     * Describes possible caching strategies for metadata.
     */
    enum class CacheStrategy {
        /**
         * Once the metadata value has been evaluated, do not re-evaluate the
         * value until it is manually invalidated.
         */
        CACHE_AFTER_FIRST_EVAL,

        /**
         * Re-evaluate the metadata item every time it is requested
         */
        NEVER_CACHE,

        /**
         * Once the metadata value has been evaluated, do not re-evaluate the
         * value in spite of manual invalidation.
         */
        CACHE_ETERNALLY
    }

    companion object {
        private val ACTUALLY_NULL = Any()
    }
}
