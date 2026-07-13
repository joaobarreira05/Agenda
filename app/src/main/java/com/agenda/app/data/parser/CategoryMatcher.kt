package com.agenda.app.data.parser

import com.agenda.app.domain.model.Category
import javax.inject.Inject

class CategoryMatcher @Inject constructor() {

    fun matchCategory(text: String, categories: List<Category>): Pair<Category?, String> {
        var resultText = text
        var matchedCategory: Category? = null

        // 1. Exact match (case insensitive)
        for (category in categories) {
            if (resultText.contains(category.name.lowercase(), ignoreCase = true)) {
                matchedCategory = category
                resultText = resultText.replace(category.name, "", ignoreCase = true)
                break
            }
        }

        // 2. Fuzzy match if exact match failed and the user said "categoria X"
        if (matchedCategory == null) {
            val regex = "categoria\\s+(\\w+)".toRegex(RegexOption.IGNORE_CASE)
            val match = regex.find(resultText)
            if (match != null) {
                val word = match.groupValues[1]
                
                // Find the closest category using Levenshtein distance
                var bestDistance = Int.MAX_VALUE
                for (category in categories) {
                    val distance = levenshtein(word.lowercase(), category.name.lowercase())
                    if (distance < bestDistance) {
                        bestDistance = distance
                        matchedCategory = category
                    }
                }
                
                // If it's reasonably close (e.g., max 3 errors depending on word length)
                if (bestDistance <= 3) {
                    resultText = resultText.replace(match.value, "", ignoreCase = true)
                } else {
                    matchedCategory = null
                }
            }
        }

        return Pair(matchedCategory, resultText.trim().replace("\\s+".toRegex(), " "))
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
