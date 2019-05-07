package com.codescrubs.rickandmortyapp.data.api.response

class PaginatedResult<T> (
    val info: Info,
    val results: List<T>
)
