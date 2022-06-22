package com.pixocial.alter.model

data class LifeCycleSortStore(
    val unitList: MutableList<LifeCycleUnit>,
    val unitChildrenMap: Map<String, MutableList<String>>
)
