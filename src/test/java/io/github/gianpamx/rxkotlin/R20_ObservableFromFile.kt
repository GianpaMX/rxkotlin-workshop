package io.github.gianpamx.rxkotlin

import io.github.gianpamx.rxkotlin.util.Urls
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.FileNotFoundException
import java.net.URL

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

    @Test
    fun shouldFailWhenBrokenFile() {
        val all = Urls.all("urls_broken.txt")

        val observer = all.test()

        observer.assertError(IllegalArgumentException::class.java)
        assertThat(observer.errors().get(0)).hasMessage("john@gmail.com")
    }

    @Test
    fun shouldParseAllUrls() {
        val all = Urls.all("urls.txt")

        val list = all
                .toList()
                .blockingGet()

        assertThat(list).hasSize(996)
        assertThat(list).startsWith(
                URL("http://www.google.com"),
                URL("http://www.youtube.com"),
                URL("http://www.facebook.com")
        )
        assertThat(list).endsWith(
                URL("http://www.king.com"),
                URL("http://www.virginmedia.com")
        )
    }
}
