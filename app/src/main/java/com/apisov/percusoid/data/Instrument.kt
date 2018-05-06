package com.apisov.percusoid.data

data class Instrument(
    val id: String? = null,
    val name: String = "",
    val inputs: List<Input> = listOf(Input())
)