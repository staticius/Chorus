package org.chorus

/**
 * An interface to describe a thread that can be interrupted.
 *
 *
 * When Server is stopping, Chorus finds all threads implements `InterruptibleThread`,
 * and interrupt them one by one.
 *
 * @see org.chorus.scheduler.AsyncWorker
 */
interface InterruptibleThread 
