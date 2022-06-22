package com.pixocial.alter.core.lifecycle.executor

import android.os.Handler
import android.os.Looper
import java.util.concurrent.*

class AlterExecutorManager private constructor() {

    var ioExecutor: ExecutorService

    var mainExecutor: Executor

    private object SingletonHolder {
        val sHolder = AlterExecutorManager()
    }

    companion object {
        val instance = SingletonHolder.sHolder
    }

    init {
        ioExecutor = Executors.newCachedThreadPool(Executors.defaultThreadFactory())

        mainExecutor = object : Executor {
            private val handler = Handler(Looper.getMainLooper())

            override fun execute(command: Runnable) {
                handler.post(command)
            }
        }
    }

}