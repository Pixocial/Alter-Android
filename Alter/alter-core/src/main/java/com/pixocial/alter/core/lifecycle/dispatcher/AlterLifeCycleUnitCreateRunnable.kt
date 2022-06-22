package com.pixocial.alter.core.lifecycle.dispatcher

import android.content.Context
import com.pixocial.alter.model.LifeCycleSortStore

class AlterLifeCycleUnitCreateRunnable(
        private val context: Context,
        private val initUnit: AlterLifeCycleDispatcherUnit,
        private val sortStore: LifeCycleSortStore,
        private val unitObjectsMap: Map<String, AlterLifeCycleDispatcherUnit>,
        private val dispatcher: IManagerDispatcher
) : Runnable {

    override fun run() {
        initUnit.toWait()
        initUnit.onCreate(context)

        dispatcher.saveInitializedUnit(initUnit.unitName)
        dispatcher.notifyChildren(initUnit, sortStore, unitObjectsMap)
    }
}