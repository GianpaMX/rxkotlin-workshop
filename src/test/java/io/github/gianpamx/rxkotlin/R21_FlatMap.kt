package io.github.gianpamx.rxkotlin

import io.github.gianpamx.rxkotlin.util.UrlDownloader
import io.github.gianpamx.rxkotlin.util.Urls
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.assertj.core.api.Assertions
import org.junit.Test

class R21_FlatMap {
    @Test
    fun shouldDownloadAllUrlsInArbitraryOrder() {
        val all = Urls.all()

        val urls: Flowable<String> = all
                .flatMap { url ->
                    UrlDownloader
                            .download(url)
                            .subscribeOn(Schedulers.io())
                }

        var bodies: List<String>? = urls
                .toList()
                .blockingGet()

        Assertions.assertThat(bodies).hasSize(996)
        Assertions.assertThat(bodies).contains("<html>www.twitter.com</html>", "<html>www.aol.com</html>", "<html>www.mozilla.org</html>")
    }
}
