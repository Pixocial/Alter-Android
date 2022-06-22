package com.pixocial.alter.plugins.utils

import org.gradle.api.Project

fun Project.isAndroidApp(): Boolean = plugins.hasPlugin("com.android.application") || plugins.hasPlugin("com.android.dynamic-feature")

fun Project.isAndroidLib(): Boolean = plugins.hasPlugin("com.android.library")