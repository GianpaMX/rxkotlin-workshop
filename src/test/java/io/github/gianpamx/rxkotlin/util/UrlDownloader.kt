package io.github.gianpamx.rxkotlin.util

import io.reactivex.Flowable
import io.reactivex.Scheduler
import org.slf4j.LoggerFactory
import java.net.URL
import java.time.Duration

object UrlDownloader {

    private val log = LoggerFactory.getLogger(UrlDownloader::class.java)

    fun download(url: URL): Flowable<String> = Flowable.fromCallable<String> { downloadBlocking(url) }

    fun downloadAsync(url: URL, scheduler: Scheduler): Flowable<String> = download(url).subscribeOn(scheduler)

    fun downloadBlocking(url: URL): String {
        log.trace("Downloading: {}", url)
        Sleeper.sleep(Duration.ofSeconds(1), Duration.ofMillis(500))
        log.trace("Done: {}", url)
        return "<html>" + url.host + "</html>"
    }
}
