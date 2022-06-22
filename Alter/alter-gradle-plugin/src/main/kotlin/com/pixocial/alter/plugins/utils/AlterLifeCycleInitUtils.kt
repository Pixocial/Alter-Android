package com.pixocial.alter.plugins.utils

import com.pixocial.alter.base.AlterLifeCycleBaseModuleTable
import com.pixocial.alter.enums.LifeCycleThread
import com.pixocial.alter.model.LifeCycleSortStore
import com.pixocial.alter.model.LifeCycleUnit
import java.lang.RuntimeException
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

fun sortInitModule(initModuleList: MutableList<AlterLifeCycleBaseModuleTable>): LifeCycleSortStore {
    val initUnitList: MutableList<LifeCycleUnit> = ArrayList()
    val iterator = initModuleList.iterator()
    val moduleNameModuleMap: MutableMap<String, AlterLifeCycleBaseModuleTable> = HashMap()

    while (iterator.hasNext()) {
        val initModule = iterator.next()
        sortUnitByPriority(initModule)
        moduleNameModuleMap[initModule.moduleName] = initModule

        initUnitList.addAll(initModule.unitList)
    }

    val stringBuilder = StringBuilder()
    stringBuilder.append("-->sortInitModule 内部排序后初始化顺序:\n")
    for (unit in initUnitList) {
        stringBuilder.append("${unit.unitName} : ${unit.priority}\n")
    }
    print(stringBuilder.toString())

    return topologySort(initUnitList)
}

private fun sortUnitByPriority(initModule: AlterLifeCycleBaseModuleTable) {
    val unitList = initModule.unitList
    if (unitList.isNotEmpty()) {
        unitList.sort()
    }
}

private fun topologySort(unitList: List<LifeCycleUnit>): LifeCycleSortStore {
    val mainResult = mutableListOf<LifeCycleUnit>()
    val ioResult = mutableListOf<LifeCycleUnit>()
    val temp = mutableListOf<LifeCycleUnit>()
    val initMap = hashMapOf<String, LifeCycleUnit>()
    val zeroDeque = ArrayDeque<String>()
    val initChildrenMap = hashMapOf<String, MutableList<String>>()
    val inDegreeMap = hashMapOf<String, Int>()

    unitList.forEach {
        val uniqueKey = it.unitName
        if (!initMap.containsKey(uniqueKey)) {
            initMap[uniqueKey] = it
            // save in-degree
            inDegreeMap[uniqueKey] = it.dependencies.size
            print("uniqueKey=$uniqueKey dependencies.size= ${it.dependencies.size} ${it.dependencies}\n")
            if (it.dependencies.size <= 0) {
                zeroDeque.offer(uniqueKey)
            } else {
                // add key parent, value list children
                it.dependencies.forEach { parent ->
                    if (initChildrenMap[parent] == null) {
                        initChildrenMap[parent] = arrayListOf()
                    }
                    initChildrenMap[parent]?.add(uniqueKey)
                    //print("parent=$parent add child= $uniqueKey allChild:${initChildrenMap[parent]}\n")
                }
            }
        } else {
            throw RuntimeException("${it.unitName} multiple add.")
        }
    }

    while (!zeroDeque.isEmpty()) {
        zeroDeque.poll()?.let {
            initMap[it]?.let { unit ->
                temp.add(unit)
                // add zero in-degree to result list
                if (unit.callCreateThread == LifeCycleThread.MAIN) {
                    mainResult.add(unit)
                } else {
                    ioResult.add(unit)
                }
            }
            initChildrenMap[it]?.forEach { children ->
                inDegreeMap[children] = inDegreeMap[children]?.minus(1) ?: 0
                // add zero in-degree to deque
                if (inDegreeMap[children] == 0) {
                    zeroDeque.offer(children)
                }
            }
        }
    }

    val stringBuilder = StringBuilder()
    stringBuilder.append("-------------------------------------------\n")
    stringBuilder.append("-->sortInitModule 拓扑排序后 IO 线程初始化顺序:\n")
    ioResult.forEach { unit ->
        stringBuilder.append("${unit.moduleName}:${unit.unitName}:${unit.priority}\n")
    }
    stringBuilder.append("-------------------------------------------\n\n")
    print(stringBuilder.toString())

    stringBuilder.clear()
    stringBuilder.append("-------------------------------------------\n")
    stringBuilder.append("-->sortInitModule 拓扑排序后 MAIN 线程初始化顺序:\n")
    mainResult.forEach { unit ->
        stringBuilder.append("${unit.moduleName}:${unit.unitName}:${unit.priority}\n")
    }
    stringBuilder.append("-------------------------------------------\n\n")
    print(stringBuilder.toString())

    initChildrenMap.forEach { (key, value) ->
        stringBuilder.clear()
        stringBuilder.append("-------------------------------------------\n")
        stringBuilder.append("-->sortInitModule 拓扑排序后，依赖 $key 的初始化单元为:\n")
        value.forEach { unitName ->
            stringBuilder.append("$unitName\n")
        }
        print(stringBuilder.toString())
    }

    if (mainResult.size + ioResult.size != unitList.size) {
        throw RuntimeException("lack of dependencies or have circle dependencies.")
    }

    val result = mutableListOf<LifeCycleUnit>().apply {
        addAll(ioResult)
        addAll(mainResult)
    }
    return LifeCycleSortStore(result, initChildrenMap)
}