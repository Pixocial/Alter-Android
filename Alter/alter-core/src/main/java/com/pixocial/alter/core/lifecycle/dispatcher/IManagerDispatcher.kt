package com.pixocial.alter.core.lifecycle.dispatcher

import android.content.Context
import com.pixocial.alter.model.LifeCycleSortStore

interface IManagerDispatcher {

    fun prepare(context: Context)

    fun dispatch(context: Context, initUnit: AlterLifeCycleDispatcherUnit, sortStore: LifeCycleSortStore, unitObjectsMap: Map<String, AlterLifeCycleDispatcherUnit>)

    fun notifyChildren(unitParent: AlterLifeCycleDispatcherUnit, sortStore: LifeCycleSortStore, unitObjectsMap: Map<String, AlterLifeCycleDispatcherUnit>)

    fun saveInitializedUnit(unitName: String)
}