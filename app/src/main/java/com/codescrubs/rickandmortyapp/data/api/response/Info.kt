package com.codescrubs.rickandmortyapp.data.api.response

data class Info(
    val count: Int,
    val pages : Int,
    val next : String?,
    val prev : String?
)