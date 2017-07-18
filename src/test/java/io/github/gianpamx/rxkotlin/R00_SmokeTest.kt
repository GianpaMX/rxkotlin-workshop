package io.github.gianpamx.rxkotlin

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.junit.Test

class R00_SmokeTest {
    @Test
    fun shouldRunRxJava() {
        val obs = Observable.just(1, 2)
        val subscriber = TestObserver<Int>()

        obs.subscribe(subscriber)

        subscriber.assertValues(1, 2)
    }

    /**
     * Note that we use TestSubscriber instead of TestObserver
     */
    @Test
    fun shouldRunRxFlowable() {
        val obs = Flowable.just<Int>(1, 2)
        val subscriber = TestSubscriber<Int>()

        obs.subscribe(subscriber)

        subscriber.assertValues(1, 2)
    }
}
