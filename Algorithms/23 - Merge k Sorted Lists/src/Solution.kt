class ListNode(var `val`: Int) {
    var next: ListNode? = null
}

class Solution {
    /**
     * Extension function to convert a List<Int> to ListNode
     */
    fun List<Int>.toListNode(): ListNode? {
        if (isEmpty()) {
            return null
        }

        val rootNode = ListNode(first())
        var currentNode = rootNode
        for (i in 1 until size) {
            currentNode.next = ListNode(this[i])
            currentNode = currentNode.next!!
        }
        return rootNode
    }

    /**
     * Extension function to convert a ListNode to List<Int>
     */
    fun ListNode.toList(): List<Int> {
        var currentNode: ListNode? = this
        val result = mutableListOf<Int>()
        while (currentNode != null) {
            result.add(currentNode.`val`)
            currentNode = currentNode.next
        }
        return result
    }

    /**
     * mergeKLists
     *
     * Simple approach which simply collects all values and does a sort
     */
    fun mergeKLists(lists: Array<ListNode?>): ListNode? {
        return lists.mapNotNull { it?.toList() }.flatten().sorted().toListNode()
    }

    /**
     * mergeKLists_Weave
     *
     * In this solution, we continuously inspect the k lists and the value at lists[i][0].
     * We then check all the k lists and take all values that match the root min value.
     * This way, we iterate through all the k lists at the same time taking the values that we need.
     */
    fun mergeKLists_Weave(lists: Array<ListNode?>): ListNode? {
        val currentLists = lists.map { it }.toMutableList()
        var minRootValue = getMinRootValue(currentLists)
        val result = mutableListOf<Int>()

        // Keep looping while there is a value at element 0 of the lists
        while (minRootValue != null) {
            currentLists.forEachIndexed { index, valuePair ->
                // Check if the next value in this list matches the min root value
                val doesMatchMinValue = valuePair?.`val` == minRootValue!!
                if (doesMatchMinValue) {
                    result.add(minRootValue!!)
                    currentLists[index] =
                        valuePair?.next // We've processed this value, so we can go to the next node
                }
            }
            minRootValue = getMinRootValue(currentLists)
        }

        return result.toListNode()
    }

    // Returns the smallest value out of all the items at element 0 in all lists
    private fun getMinRootValue(lists: List<ListNode?>): Int? =
        lists.mapNotNull { it?.`val` }.min()
}
