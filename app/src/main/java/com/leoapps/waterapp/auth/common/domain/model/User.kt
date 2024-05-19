package com.leoapps.waterapp.auth.common.domain.model

data class User(
    val id: String,
    val email: String?,
    val name: String?,
//    val goal: Goal?,
//    val waterEntries: List<WaterEntry>,
)