package com.pixocial.alter.core.lifecycle.dispatcher

interface IDispatcher {

    fun callCreateOnMainThread(): Boolean

    fun toWait()

    fun toNotify()

}