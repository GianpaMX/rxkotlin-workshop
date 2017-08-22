package io.github.gianpamx.rxkotlin

import io.github.gianpamx.rxkotlin.util.UrlDownloader
import io.github.gianpamx.rxkotlin.util.Urls
import io.reactivex.schedulers.Schedulers
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.net.URI

class R21_FlatMap {
    @Test
    fun shouldDownloadAllUrlsInArbitraryOrder() {
        val urls = Urls.all()


        var bodies = urls
                .flatMap { url -> UrlDownloader.downloadAsync(url, Schedulers.io()) }
                .toList()
                .blockingGet()

        assertThat(bodies).hasSize(996)
        assertThat(bodies).contains("<html>www.twitter.com</html>", "<html>www.aol.com</html>", "<html>www.mozilla.org</html>")
    }

    @Test
    fun shouldDownloadAllUrls() {
        val urls = Urls.all()

        var bodies = urls
                .flatMap { url ->
                    UrlDownloader
                            .downloadAsync(url, Schedulers.io())
                            .map { html -> Pair(url.toURI(), html) }
                }
                .toMap({ pair -> pair.first }, { pair -> pair.second })
                .blockingGet()

        assertThat(bodies).hasSize(996)
        assertThat(bodies).containsEntry(URI("http://www.twitter.com"), "<html>www.twitter.com</html>")
        assertThat(bodies).containsEntry(URI("http://www.aol.com"), "<html>www.aol.com</html>")
        assertThat(bodies).containsEntry(URI("http://www.mozilla.org"), "<html>www.mozilla.org</html>")
    }
}
