package com.ctb.domain.models

data class EvolutionStep(
    val id: Int,
    val name: String,
    val imageUrl: String,
)

data class EvolutionChain(val steps: List<EvolutionStep>)
