import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SolutionTest {

    companion object {
        private val solution = Solution()
    }

    @Nested
    inner class MaxEnvelopesTests {
        @Test
        fun `example 1`() {
            val envelopes = arrayOf(
                intArrayOf(5, 4),
                intArrayOf(6, 4),
                intArrayOf(6, 7),
                intArrayOf(2, 3)
            )
            assertEquals(3, solution.maxEnvelopes(envelopes))
        }

        @Test
        fun `example 2 - all duplicates`() {
            val envelopes = arrayOf(
                intArrayOf(1, 1),
                intArrayOf(1, 1),
                intArrayOf(1, 1)
            )
            assertEquals(1, solution.maxEnvelopes(envelopes))
        }

        @Test
        fun `empty input`() {
            val envelopes = emptyArray<IntArray>()
            assertEquals(0, solution.maxEnvelopes(envelopes))
        }

        @Test
        fun `same width different heights - cannot chain`() {
            val envelopes = arrayOf(
                intArrayOf(5, 4),
                intArrayOf(5, 5),
                intArrayOf(5, 3)
            )
            assertEquals(1, solution.maxEnvelopes(envelopes))
        }

        @Test
        fun `increasing chain with noise`() {
            val envelopes = arrayOf(
                intArrayOf(2, 3),
                intArrayOf(3, 4), intArrayOf(3, 3),
                intArrayOf(4, 6),
                intArrayOf(5, 5), intArrayOf(5, 4),
                intArrayOf(6, 7), intArrayOf(6, 5),
                intArrayOf(7, 8)
            )
            assertEquals(5, solution.maxEnvelopes(envelopes))
        }
    }

    @Nested
    inner class LongestIncreasingSubsequenceTailTests {

        @Test
        fun `empty list`() {
            val result = solution.longestIncreasingSubsequenceTail(emptyList())
            assertEquals(emptyList(), result)
            assertEquals(0, result.size)
        }

        @Test
        fun `single element`() {
            val result = solution.longestIncreasingSubsequenceTail(listOf(42))
            assertEquals(listOf(42), result)
            assertEquals(1, result.size)
        }

        @Test
        fun `strictly increasing`() {
            val input = listOf(1, 2, 3, 4, 5)
            val result = solution.longestIncreasingSubsequenceTail(input)
            assertEquals(listOf(1, 2, 3, 4, 5), result)
            assertEquals(5, result.size)
        }

        @Test
        fun `strictly decreasing`() {
            // Tails array keeps shrinking → only [1] left
            val input = listOf(5, 4, 3, 2, 1)
            val result = solution.longestIncreasingSubsequenceTail(input)

            assertEquals(listOf(1), result)
            assertEquals(1, result.size)
        }

        @Test
        fun `all duplicates`() {
            val input = listOf(7, 7, 7, 7)
            val result = solution.longestIncreasingSubsequenceTail(input)

            assertEquals(listOf(7), result)
            assertEquals(1, result.size)
        }

        @Test
        fun `classic example`() {
            val input = listOf(10, 9, 2, 5, 3, 7, 101, 18)
            val result = solution.longestIncreasingSubsequenceTail(input)

            assertEquals(listOf(2, 3, 7, 18), result)
            assertEquals(4, result.size)
        }

        @Test
        fun `zigzag sequence`() {
            // One possible tails = [-1, 0, 2, 3]
            val input = listOf(3, 4, -1, 0, 6, 2, 3)
            val result = solution.longestIncreasingSubsequenceTail(input)

            assertEquals(listOf(-1, 0, 2, 3), result)
            assertEquals(4, result.size)
        }

        @Test
        fun `plateaus with rises`() {
            val input = listOf(1, 1, 1, 2, 2, 3, 3)
            val result = solution.longestIncreasingSubsequenceTail(input)

            assertEquals(listOf(1, 2, 3), result)
            assertEquals(3, result.size)
        }

        @Test
        fun `noise before long run`() {
            val input = listOf(10, 5, 8, 3, 9, 4, 12, 11, 5, 6, 7, 8, 9, 10)
            val result = solution.longestIncreasingSubsequenceTail(input)

            assertEquals(listOf(3, 4, 5, 6, 7, 8, 9, 10), result)
            assertEquals(8, result.size)
        }
    }
}