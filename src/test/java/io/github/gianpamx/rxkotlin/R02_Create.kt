package io.github.gianpamx.rxkotlin

import io.github.gianpamx.rxkotlin.util.Sleeper.sleep
import io.github.gianpamx.rxkotlin.util.Threads.runInBackground
import io.reactivex.Observable
import org.awaitility.Awaitility.await
import org.junit.Test
import org.mockito.Mockito.*
import org.slf4j.LoggerFactory
import java.sql.SQLException
import java.time.Duration
import javax.sql.DataSource

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
            sleep(Duration.ofSeconds(2))
            sub.onComplete()
            log.info("Completed")
        }
        log.info("Subscribing")
        obs.subscribe()
        log.info("Result")
    }

    @Test
    fun createLambdaIsInvokedManyTimes() {
        val ds = mock(DataSource::class.java)

        val obs = queryDatabase(ds)

        obs.subscribe()
        obs.subscribe()

        verify(ds, times(2)).getConnection()
    }

    @Test
    fun cachingWhenCreateIsInvokedManyTimes() {
        val ds = mock(DataSource::class.java)

        val obs = queryDatabase(ds).cache()

        obs.subscribe()
        obs.subscribe()

        verify(ds, times(1)).getConnection()
    }


    fun queryDatabase(ds: DataSource): Observable<Int> {
        return Observable.create<Int> { sub ->
            try {
                ds.connection.use { conn -> sub.onComplete() }
            } catch (e: SQLException) {
                sub.onError(e)
            }
        }
    }

    @Test
    fun infiniteObservable() {
        val obs = Observable.create<Int>({ sub ->
            var i = 0
            while (!sub.isDisposed) {
                sub.onNext(i++)
            }
        })

        val subscriber = obs
                .skip(10)
                .take(3)
                .test()
        subscriber
                .assertValues(10, 11, 12)
                .assertComplete()
    }

    @Test
    fun infiniteObservableInBackground() {
        val obs = Observable.create<Int> { sub ->
            runInBackground(Runnable {
                var i = 0
                while (!sub.isDisposed) {
                    sub.onNext(i++)
                }
            })
        }

        val subscriber = obs
                .skip(10)
                .take(3)
                .test()

        await().until({ subscriber.assertValues(10, 11, 12) } as () -> Unit)
    }
}
