package com.agenda.app.data.parser

import com.agenda.app.domain.model.Category
import javax.inject.Inject

class CategoryMatcher @Inject constructor() {

    fun matchCategory(text: String, categories: List<Category>): Pair<Category?, String> {
        var matchedCategory: Category? = null

        // 1. Exact match (case insensitive)
        for (category in categories) {
            if (text.contains(category.name.lowercase(), ignoreCase = true)) {
                return Pair(category, text)
            }
        }

        // 2. Fuzzy match across all words
        val words = text.split("\\s+".toRegex())
        var bestDistance = Int.MAX_VALUE
        
        for (word in words) {
            val cleanWord = word.replace(Regex("[^\\p{L}\\p{Nd}]"), "") // Only letters and numbers
            if (cleanWord.length < 2) continue

            for (category in categories) {
                val catName = category.name.trim().lowercase()
                val distance = levenshtein(cleanWord.lowercase(), catName)
                
                // Be more lenient: distance 1 for length 2-3, distance 2 for 4-6, distance 3 for > 6
                val maxAllowedDistance = when {
                    catName.length <= 3 -> 1
                    catName.length <= 6 -> 2
                    else -> 3
                }
                
                if (distance <= maxAllowedDistance && distance < bestDistance) {
                    bestDistance = distance
                    matchedCategory = category
                }
            }
        }

        return Pair(matchedCategory, text)
    }

    private fun levenshtein(lhs: CharSequence, rhs: CharSequence): Int {
        val lhsLength = lhs.length
        val rhsLength = rhs.length

        var cost = Array(lhsLength + 1) { it }
        var newCost = Array(lhsLength + 1) { 0 }

        for (i in 1..rhsLength) {
            newCost[0] = i
            for (j in 1..lhsLength) {
                val match = if (lhs[j - 1] == rhs[i - 1]) 0 else 1
                val costReplace = cost[j - 1] + match
                val costInsert = cost[j] + 1
                val costDelete = newCost[j - 1] + 1
                newCost[j] = minOf(costInsert, costDelete, costReplace)
            }
            val swap = cost
            cost = newCost
            newCost = swap
        }
        return cost[lhsLength]
    }
}
