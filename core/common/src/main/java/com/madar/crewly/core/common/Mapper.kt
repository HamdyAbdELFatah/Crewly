package com.madar.crewly.core.common

interface Mapper<I, O> {
    fun map(input: I): O
}

fun <I, O> Mapper<I, O>.mapList(input: List<I>): List<O> = input.map { map(it) }
