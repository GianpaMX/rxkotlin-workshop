package io.github.gianpamx.rxkotlin.util

import org.slf4j.LoggerFactory

import java.time.Duration
import java.util.Random
import java.util.concurrent.TimeUnit

object Sleeper {

    private val log = LoggerFactory.getLogger(Sleeper::class.java)
    val RAND = Random()

    fun sleep(duration: Duration, stdDev: Duration) {
        val randMillis = Math.max(0.0, duration.toMillis() + RAND.nextGaussian() * stdDev.toMillis())
        sleep(Duration.ofMillis(randMillis.toLong()))
    }

    fun sleep(duration: Duration) {
        try {
            TimeUnit.MILLISECONDS.sleep(duration.toMillis())
        } catch (e: InterruptedException) {
            log.warn("Sleep interrupted", e)
        }

    }
}
