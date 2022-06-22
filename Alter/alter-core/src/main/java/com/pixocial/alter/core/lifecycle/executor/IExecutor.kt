package com.pixocial.alter.core.lifecycle.executor

import java.util.concurrent.Executor

interface IExecutor {

    fun createExecutor(): Executor

}