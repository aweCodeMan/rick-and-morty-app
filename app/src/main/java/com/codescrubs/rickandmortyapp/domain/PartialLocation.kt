package com.codescrubs.rickandmortyapp.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PartialLocation(
    val name: String,
    val url: String
) : Parcelable
