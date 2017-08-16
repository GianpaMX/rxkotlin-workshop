package io.github.gianpamx.rxkotlin.util

import io.reactivex.Flowable
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors.toList

object Urls {
    fun all(): Flowable<URL> = all("urls.txt")

    fun all(filename: String): Flowable<URL> = Flowable.defer<URL> {
        load(filename)
    }

    fun load(filename: String): Flowable<URL> = Flowable.fromIterable(classpathReaderOf(filename).lines().map<URL> { l ->
        try {
            URL(l)
        } catch (e: Exception) {
            throw IllegalArgumentException(l)
        }
    }.collect(toList()))

    fun classpathReaderOf(filename: String): BufferedReader = BufferedReader(InputStreamReader(openInputStream(filename), StandardCharsets.UTF_8))

    fun openInputStream(filename: String) = (Urls::class.java.getResource(filename) ?: throw FileNotFoundException(filename)).openStream()
}
