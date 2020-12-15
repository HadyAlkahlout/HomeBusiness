package com.nurbk.ps.homebusness.model.Country

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

data class Continent(
    val continentName: String,
    val questions: List<Region>
) : ExpandableGroup<Region>(continentName, questions)