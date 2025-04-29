package org.chorus_oss.chorus.metadata

/**
 * A MetadataEvaluationException is thrown any time a [ ] fails to evaluate its value due to an exception. The
 * originating exception will be included as this exception's cause.
 */
class MetadataEvaluationException internal constructor(cause: Throwable?) : RuntimeException(cause)