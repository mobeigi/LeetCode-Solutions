class Solution {

    fun findSubstring(s: String, words: Array<String>): List<Int> {
        if (words.isEmpty()) {
            return emptyList()
        }

        // Find useful lengths we will be working with
        val wordLength = words.first().length
        val concatenatedSubstringLength = wordLength * words.size

        // Early exit optimization for impossible case
        if (s.length < concatenatedSubstringLength) {
            return emptyList()
        }

        val solutionIndices = mutableListOf<Int>()

        // Create frequency map for our words
        val wordFrequency = HashMap<String, Int>()
        words.forEach { word ->
            wordFrequency[word] = wordFrequency.getOrDefault(word, 0) + 1
        }

        // We will iterate over all possible substrings that can possibly be valid solutions
        val lastPossibleStartingIndex = s.length - concatenatedSubstringLength
        for (i in 0 .. lastPossibleStartingIndex) {
            val subString = s.substring(i, i+concatenatedSubstringLength)
            val currentFrequency = HashMap<String,Int>()

            run earlyExit@{
                // Count number of matching words by building a frequency map for this substring
                var matchedWordCount = 0
                subString.chunkedSequence(wordLength).forEach { chunkedWord ->
                    if (!wordFrequency.contains(chunkedWord)) {
                        return@earlyExit
                    }
                    currentFrequency[chunkedWord] = currentFrequency.getOrDefault(chunkedWord, 0) + 1
                    if (currentFrequency[chunkedWord]!! > wordFrequency[chunkedWord]!!) {
                        return@earlyExit
                    }
                    ++matchedWordCount
                }

                if (matchedWordCount == words.size) {
                    solutionIndices.add(i)
                }
            }
        }

        return solutionIndices
    }
}
