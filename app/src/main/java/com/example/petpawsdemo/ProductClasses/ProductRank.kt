package com.example.petpawsdemo.ProductClasses

import com.example.petpawsdemo.distanceTo
import kotlin.math.min

class RankCoefficients{
    companion object {
        val typeCoefficient = 40
        val subtypeCoefficient = 30
        val nameCoefficient = 20
        val tagCoefficient = 30
    }
}

// the search cost is a measure of how far away a query is from this product.
// it is a hand-crafted heuristic that can return incorrect results, but is a
// big step-up from traditional exact-match searches.
// the distance to the type should be the most important
// followed by the subtype. E.g. dog toys
// it is expected that all keywords are lowercase when passed into this function
fun Product.searchCost(keywords: List<String>): Int{
    var typeDistance = Int.MAX_VALUE
    var subtypeDistance = Int.MAX_VALUE
    val nameWords = name.split(" ").map{it.lowercase()}
    var nameDistance = 0
    var tagDistance = 0
    keywords.forEach{ word ->
        typeDistance = min(typeDistance, word distanceTo productCategory.type)
        subtypeDistance = min(subtypeDistance, word distanceTo productCategory.subtype)
        var currentNameDist = Int.MAX_VALUE
        nameWords.forEach{name->
            currentNameDist = min(currentNameDist, word distanceTo name)
        }
        nameDistance += currentNameDist
        var currentTagDist = Int.MAX_VALUE
        tags.forEach{tag->
            currentTagDist = min(currentTagDist, word distanceTo tag)
        }
        tagDistance += currentTagDist
    }
    return  typeDistance * RankCoefficients.typeCoefficient +
            subtypeDistance * RankCoefficients.subtypeCoefficient +
            nameDistance * RankCoefficients.nameCoefficient +
            tagDistance * RankCoefficients.tagCoefficient
}