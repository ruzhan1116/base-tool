package com.future.email

import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

class MailExecutors {

    companion object {
        private const val THREAD_COUNT = 2

        private var INSTANCE: MailExecutors? = null

        @JvmStatic
        fun get() = INSTANCE ?: synchronized(MailExecutors::class.java) {
            INSTANCE ?: MailExecutors().also {
                INSTANCE = it
            }
        }
    }

    private val diskIO = Executors.newFixedThreadPool(THREAD_COUNT, object :
        ThreadFactory {
        private val THREAD_NAME_STEM = "arch_disk_io_%d"

        private val mThreadId = AtomicInteger(0)

        override fun newThread(r: Runnable): Thread {
            val thread = Thread(r)
            thread.name = String.format(THREAD_NAME_STEM, mThreadId.getAndIncrement())
            return thread
        }
    })

    fun executeOnDiskIO(runnable: Runnable) {
        diskIO.execute(runnable)
    }
}