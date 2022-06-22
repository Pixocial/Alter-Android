package com.pixocial.alter.base

import com.pixocial.alter.model.LifeCycleUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

abstract class AlterLifeCycleBaseModuleTable {

    var moduleName: String = ""
    var unitList = ArrayList<LifeCycleUnit>()

    fun addLifeCycleUnit(lifeCycleUnit: LifeCycleUnit) {
        unitList.add(lifeCycleUnit)
    }
}