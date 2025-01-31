package com.jidasea.poliexplore.models

data class Place (
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String,
    val funFacts: List<String>
)