package com.pixocial.alter.module_base

import android.os.Bundle

val hashMap = hashMapOf(
    "withBooleanArray" to booleanArrayOf(true, false),
    "withBoolean" to false,
    "withBundle" to Bundle().apply {
        putString("fsdf", "fdsff")
    },
    "withByte" to "123".toByte(),
    "withByteArray" to "233".toByteArray(charset("utf-8")),
    "withChar" to 'a',
    "withCharArray" to charArrayOf('a', 'b'),
    "withDouble" to 0.432434,
    "withDoubleArray" to doubleArrayOf(0.432424, 1.23241242323),
    "withFlags" to 1,
    "withFloat" to 0.43f,
    "withFloatArray" to floatArrayOf(1.00f, 0.21f),
    "withInt" to 1,
    "withIntArray" to intArrayOf(0, 1, 2, 3),
    "withIntegerArrayList" to arrayListOf(123246334, 423324324),
    "withLong" to 10000000000L,
    "withLongArray" to longArrayOf(92233203685477580L),
    "withShort" to 12.toShort(),
    "withShortArray" to shortArrayOf(1.toShort(), 2.toShort()),
    "withString" to "str",
    "withStringArray" to arrayOf("fdsf", "fsdfdsf"),
    "withStringArrayList" to arrayListOf("fsdf", "fsdfsdf")
)