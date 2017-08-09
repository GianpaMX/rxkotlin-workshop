package io.github.gianpamx.rxkotlin

import com.google.common.util.concurrent.ThreadFactoryBuilder
import io.github.gianpamx.rxkotlin.util.Sleeper
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.junit.Test
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.time.Duration.ofMillis
import java.time.Duration.ofSeconds
import java.util.concurrent.Executors

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

    @Test
    fun observeOn() {
        slowFromCallable()
                .subscribeOn(Schedulers.io())
                .doOnNext { x -> log.info("A: {}", x) }
                .observeOn(Schedulers.computation())
                .doOnNext { x -> log.info("B: {}", x) }
                .observeOn(Schedulers.newThread())
                .doOnNext { x -> log.info("C: {}", x) }
                .subscribe {
                    x ->
                    log.info("Got: {}", x)
                }
        Sleeper.sleep(ofMillis(1_100));
    }

    @Test
    fun customExecutor() {
        val subscriber = slowFromCallable()
                .subscribeOn(myCustomScheduler())
                .test()

        await().until({
            val lastThread = subscriber.lastThread()
            assertThat(lastThread).isNotNull()
            assertThat(lastThread.name).startsWith("CustomExecutor-")
        } as () -> Unit)
    }

    private fun myCustomScheduler(): Scheduler {
        val threadFactory = ThreadFactoryBuilder()
                .setNameFormat("CustomExecutor-%d")
                .build()

        val executorService = Executors.newFixedThreadPool(10, threadFactory)

        return Schedulers.from(executorService)
    }
}
