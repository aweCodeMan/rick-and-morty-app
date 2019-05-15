package com.codescrubs.rickandmortyapp

import com.codescrubs.rickandmortyapp.data.api.response.Info
import com.codescrubs.rickandmortyapp.data.api.response.PaginatedResult
import org.junit.Test

import org.junit.Assert.*

//  This is here so I can show that I know how to create tests
class PaginatedResultTest {

    @Test
    fun hasNextPageIsFalseWhenNextURLIsMissing() {
        val paginatedResult = PaginatedResult<String>(Info(1, 1, null, null), arrayListOf())
        assertFalse(paginatedResult.hasNextPage())
    }

    @Test
    fun hasNextPageWhenNextURLIsPresent() {
        val paginatedResult = PaginatedResult<String>(Info(1, 1, "http://example.com", null), arrayListOf())
        assertTrue(paginatedResult.hasNextPage())
    }
}
