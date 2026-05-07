package com.example.nightsky.models

import java.io.Serializable

data class `Observation.kt`(
    val id: Int,
    val astroName: String,
    val category: String,
    val timestamp: String,
    val latitude: Double,
    val longitude: Double,
    val photoUri: String?,
    val notes: String
) : Serializable

