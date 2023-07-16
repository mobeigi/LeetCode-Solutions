/**
 * Solution
 *
 * We use two maps to allow us to lookup keys by string in O(1) and lookup by frequency in O(1).
 * We also maintain a key count for the minimum and maximum at all times.
 * We maintain the maximum and minimum as we increment and decrement.
 *
 * The getMaxKey and getMinKey are guaranteed to always be in O(1).
 * inc are guaranteed to always be in O(1).
 * However, dec will usually be in O(1) but will be in O(N) in the case where the minimum key is dec to 0 and
 * removed entirely. This will trigger an O(N) search for the next minimum key.
 */
class AllOne {
    private val freq = HashMap<String, Long>()
    private val countMap = HashMap<Long, MutableSet<String>>()

    data class KeyCount(var key: String, var count: Long)
    private fun KeyCount.isEmpty() = key == ""

    private val maxKC = KeyCount("", 0L)
    private val minKC = KeyCount("", Long.MAX_VALUE)

    fun inc(key: String) {
        // Update maps to be consistent
        val prevCount = freq.getOrDefault(key, 0)
        val newCount = prevCount + 1

        freq[key] = newCount

        removePreviousCountMap(key, prevCount)
        addNewCountMap(key, newCount)

        // If max or min is empty, set them to this key
        if (maxKC.isEmpty()) {
            maxKC.key = key
            maxKC.count = newCount
        }
        if (minKC.isEmpty()) {
            minKC.key = key
            minKC.count = newCount
        }

        // Inc the maxKey keeps it as max and updates its count
        if (key == maxKC.key) {
            maxKC.count = newCount
        }

        // Check for a new maxKey
        if (key != maxKC.key && newCount > maxKC.count) {
            maxKC.key = key
            maxKC.count = newCount
        }

        // Check to see if we maintain same minKey
        if (key == minKC.key) {
            // Check if another min key exists at prevCount
            if (countMap.containsKey(prevCount) && countMap[prevCount]?.isNotEmpty()!!) {
                minKC.key = countMap[prevCount]?.first().toString()
            } else {
                minKC.key = key
                minKC.count = newCount
            }
        }

        // Check for a new minKey
        if (key != minKC.key && newCount < minKC.count) {
            minKC.key = key
            minKC.count = newCount
        }
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

        // Check for new maxKey
        if (key == maxKC.key) {
            // Check if other max exists at prevCount
            if (countMap.containsKey(prevCount) && countMap[prevCount]?.isNotEmpty()!!) {
                maxKC.key = countMap[prevCount]?.first().toString()
            } else {
                if (newCount == 0L) {
                    // No other keys exist so no max or min
                    maxKC.key = ""
                    maxKC.count = 0L

                    minKC.key = ""
                    minKC.count = Long.MAX_VALUE

                    return
                } else {
                    maxKC.key = key
                    maxKC.count = newCount
                }
            }
        }

        // Check for new minKey
        if (key == minKC.key) {
             if (newCount == 0L) {
                 // We've lost the min key and need to search for next one in O(N)
                 val nextMin = countMap.minBy { it.key }
                 minKC.key = nextMin?.value?.first() ?: ""
                 minKC.count = nextMin?.key ?: Long.MAX_VALUE
             } else {
                 // Otherwise count just goes down
                 minKC.count = newCount
             }
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
        return maxKC.key
    }

    fun getMinKey(): String {
        return minKC.key
    }
}
