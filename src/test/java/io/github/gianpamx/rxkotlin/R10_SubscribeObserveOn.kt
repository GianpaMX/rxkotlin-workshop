package io.github.gianpamx.rxkotlin

import io.github.gianpamx.rxkotlin.util.Sleeper
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.time.Duration.ofMillis
import java.time.Duration.ofSeconds

class R10_SubscribeObserveOn {
    val log = LoggerFactory.getLogger(R10_SubscribeObserveOn::class.java)

    @Test
    fun subscribeOn() {
        val obs = slowFromCallable()

        obs
                .subscribeOn(Schedulers.io())
                .subscribe { x -> log.info("Got: {}", x) }

        Sleeper.sleep(ofMillis(1_100))
    }

    fun slowFromCallable(): Flowable<BigDecimal> {
        return Flowable.fromCallable<BigDecimal> {
            log.info("Starting")
            Sleeper.sleep(ofSeconds(1))
            log.info("Done")

            BigDecimal.TEN
        }
    }
}
