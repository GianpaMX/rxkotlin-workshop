package io.github.gianpamx.rxkotlin

import io.github.gianpamx.rxkotlin.util.Sleeper
import io.reactivex.Observable
import org.junit.Test
import org.slf4j.LoggerFactory
import java.time.Duration

class R02_Create {
    private val log = LoggerFactory.getLogger(R02_Create::class.java)

    @Test
    fun observableUsingCreate() {
        val obs = Observable.create<String> { emitter ->
            emitter.onNext("A")
            emitter.onNext("B")
            emitter.onComplete()
        }

        obs
                .test()
                .assertValues("A", "B")
                .assertComplete()
    }

    @Test
    fun sameThread() {
        val curThreadName = Thread.currentThread().name

        val obs = Observable.create<String> { sub ->
            sub.onNext(Thread.currentThread().name)
            sub.onComplete()
        }

        obs
                .test()
                .assertValue(curThreadName)
    }

    @Test
    fun createCanBeBlocking() {
        log.info("Start")
        val obs = Observable.create<String> { sub ->
            log.info("In create()")
            Sleeper.sleep(Duration.ofSeconds(2))
            sub.onComplete()
            log.info("Completed")
        }
        log.info("Subscribing")
        obs.subscribe()
        log.info("Result")
    }
}
