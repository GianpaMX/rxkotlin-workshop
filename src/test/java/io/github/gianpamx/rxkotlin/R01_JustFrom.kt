package io.github.gianpamx.rxkotlin

import io.reactivex.Flowable
import org.junit.Test

class R01_JustFrom {
    @Test
    fun shouldCreateFlowableFromConstants() {
        val obs = Flowable.just("A", "B", "C")

        obs.subscribe { s ->
            println("Got: $s")
        }
    }
}
