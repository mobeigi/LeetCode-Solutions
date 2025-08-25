class Solution {
    /**
     *  Given a set of envelopes, each represented as [width, height], returns
     *  the maximum number of envelopes that can be nested one inside another.
     *  Sort by (w↑, h↓), then run LIS on heights.
     *
     * Time: O(n log n) where n is envelopes size
     * Space: O(n)
     */
    fun maxEnvelopes(envelopes: Array<IntArray>): Int {
        if (envelopes.isEmpty()) {
            return 0
        }

        // Deterministic sort
        val sortedEnvelopes = envelopes.sortedWith(
            compareBy<IntArray> { it[0]} // width asc
                .thenByDescending { it[1] } // height desc
            )
        val heights = sortedEnvelopes.map { it[1] }
        return longestIncreasingSubsequenceTail(heights).size
    }

    /**
     * Computes the Longest Increasing Subsequence (LIS) using the "tails array" method.
     *
     * Time: O(n log n) where n is input list size
     * Space: O(n) where input in strictly increasing
     */
    fun longestIncreasingSubsequenceTail(input: List<Int>): List<Int> {
        if (input.isEmpty()) {
            return emptyList()
        }

        val tails = mutableListOf<Int>()
        for (cur in input) {
            val pos = tails.binarySearch(cur)
            val insertionPos = if (pos >= 0) pos else -pos - 1
            if (insertionPos == tails.size) {
                // Append case
                tails.add(cur)
            } else {
                // Replace case
                tails[insertionPos] = cur
            }
        }

        return tails
    }
}

fun main() {
    val envelopes = arrayOf(
        intArrayOf(2, 3),
        intArrayOf(3, 4), intArrayOf(3, 3),
        intArrayOf(4, 6),
        intArrayOf(5, 5), intArrayOf(5, 4),
        intArrayOf(6, 7), intArrayOf(6, 5), intArrayOf(6, 4),
        intArrayOf(7, 8), intArrayOf(7, 7),
        intArrayOf(8, 9),
        intArrayOf(9, 10), intArrayOf(9, 9),
        intArrayOf(10, 11)
    )

    val result = Solution().maxEnvelopes(envelopes)
    println("Max envelopes: $result") // Expected: 8
}