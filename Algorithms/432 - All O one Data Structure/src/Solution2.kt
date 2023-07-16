/**
 * Solution2
 *
 * We use two maps to allow us to lookup keys by string in O(1) and lookup by frequency in O(1).
 * This solution does NOT meet the requirements as getMaxKey and getMinKey are both O(N) functions.
 * However, this solution is simple to read and understand and is quite performant nevertheless.
 */
class AllOne2 {
    private val freq = HashMap<String, Long>()
    private val countMap = HashMap<Long, MutableSet<String>>()

    fun inc(key: String) {
        val prevCount = freq.getOrDefault(key, 0)
        val newCount = prevCount + 1

        freq[key] = newCount

        removePreviousCountMap(key, prevCount)
        addNewCountMap(key, newCount)
    }

    fun dec(key: String) {
        val prevCount = freq[key]!!
        val newCount = prevCount - 1

        removePreviousCountMap(key, prevCount)

        if (newCount == 0L) {
            freq.remove(key)
        } else {
            freq[key] = newCount
            addNewCountMap(key, newCount)
        }
    }

    private fun removePreviousCountMap(key: String, prevCount: Long) {
        countMap[prevCount]?.remove(key)
        if (countMap[prevCount].isNullOrEmpty()) {
            countMap.remove(prevCount)
        }
    }

    private fun addNewCountMap(key: String, newCount: Long) {
        val newSet = countMap.getOrDefault(newCount, mutableSetOf())
        newSet.add(key)
        countMap[newCount] = newSet
    }

    fun getMaxKey(): String {
        return countMap.maxBy { it.key }?.value?.first() ?: ""
    }

    fun getMinKey(): String {
        return countMap.minBy { it.key }?.value?.first() ?: ""
    }
}
