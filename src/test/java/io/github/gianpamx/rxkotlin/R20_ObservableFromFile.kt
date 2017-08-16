package io.github.gianpamx.rxkotlin

import io.github.gianpamx.rxkotlin.util.Urls
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.FileNotFoundException

class R20_ObservableFromFile {
    @Test
    fun shouldNotFailWithoutDisposable() {
        val all = Urls.all("urrrrrls.txt")
    }

    @Test
    fun shouldFailWhenMissingFile() {
        val all = Urls.all("urrrrrls.txt")

        val observer = all.test()

        observer.assertNoValues()
        observer.assertError(FileNotFoundException::class.java)

        assertThat(observer.errors().get(0)).hasMessageContaining("urrrrrls.txt")
    }
}
