package com.codescrubs.rickandmortyapp.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Character(
    val id: Long,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: PartialLocation,
    val location: PartialLocation,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
) : Parcelable