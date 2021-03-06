package com.codescrubs.rickandmortyapp.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Location(
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>,
    val url: String
) : Parcelable
