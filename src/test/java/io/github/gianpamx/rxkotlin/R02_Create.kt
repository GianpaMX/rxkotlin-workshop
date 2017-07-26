package io.github.gianpamx.rxkotlin

import io.reactivex.Observable
import org.junit.Test

class R02_Create {
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
}
