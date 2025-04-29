package org.chorus_oss.chorus

/**
 * An interface to describe a thread that can be interrupted.
 *
 *
 * When Server is stopping, Chorus finds all threads implements `InterruptibleThread`,
 * and interrupt them one by one.
 *
 * @see org.chorus_oss.chorus.scheduler.AsyncWorker
 */
interface InterruptibleThread 
