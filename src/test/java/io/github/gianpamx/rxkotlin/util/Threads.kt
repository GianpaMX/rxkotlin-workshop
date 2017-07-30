package io.github.gianpamx.rxkotlin.util

object Threads {
    fun runInBackground(runnable: Runnable): Thread {
        val thread = Thread(runnable)
        return start(thread)
    }

    private fun start(thread: Thread): Thread {
        thread.start()
        return thread
    }
}
