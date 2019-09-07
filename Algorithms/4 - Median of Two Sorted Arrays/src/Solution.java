import java.util.Arrays;
import java.util.stream.IntStream;

class Solution {
    // Kth smallest integer solution
    // T: O(log(n+m)), S: O(1)
    public double findMedianSortedArrays(int[] nums1, int[] nums2)
    {
        // Empty array case
        if (nums1.length == 0 && nums2.length == 0)
            return 0.0;

        int i=0, j=0;
        int combinedLength = nums1.length + nums2.length;

        // Iterate n+m/2 times over both sorted lists
        // We are basically counting here to reach the n+m/2 element
        while (i+j < (combinedLength-1)/2)
        {
            if (i > nums1.length - 1)
                ++j;
            else if (j > nums2.length - 1)
                ++i;
            else
            {
                if (nums1[i] <= nums2[j])
                    ++i;
                else
                    ++j;
            }
        }

        // At this point, either i or j point to the element at (n+m)/2
        // Thus the lower value element at these indexes in each array is the first median value
        int firstMedianValue;

        // Handle array edge case
        if (i > nums1.length-1)
            firstMedianValue = nums2[j++];
        else if (j > nums2.length-1)
            firstMedianValue = nums1[i++];
        else
        {
            firstMedianValue = Math.min(nums1[i], nums2[j]);
            if (nums1[i] <= nums2[j])
                ++i;
            else
                ++j;
        }

        // Odd combined length = single median
        if (combinedLength % 2 != 0)
            return firstMedianValue;
        // Even combined length = two medians that should be averaged
        else
        {
            // Handle end of array edge case
            if (i > nums1.length-1)
                return (firstMedianValue + nums2[j]) / 2.0;
            else if (j > nums2.length-1)
                return (firstMedianValue + nums1[i]) / 2.0;
            else
                return (firstMedianValue + Math.min(nums1[i], nums2[j])) / 2.0;
        }
    }

    // Compute median of single sorted input array
    private static double computeMedian(int[] array)
    {
        if (array.length == 0)
            return 0.0;
        else if (array.length % 2 == 0)
            return (array[array.length/2] + array[(array.length/2)-1]) / 2.0;
        else
            return array[array.length/2];
    }

    // Initial attempt
    public double findMedianSortedArrays1(int[] nums1, int[] nums2)
    {
        // Add both lists together
        int[] combined = IntStream.concat(Arrays.stream(nums1), Arrays.stream(nums2)).toArray();

        // Sort list in O(log (n+m))
        Arrays.sort(combined);

        // Return median
        if (combined.length % 2 == 0)
            return ((double)combined[combined.length / 2] + combined[(combined.length / 2) - 1]) / 2;
        else
            return combined[combined.length / 2];
    }

    private static void test()
    {
        Solution solution = new Solution();
        assert(solution.findMedianSortedArrays(new int[]{}, new int[]{10,11,12,13}) == 11.5);
        assert(solution.findMedianSortedArrays(new int[]{}, new int[]{2,3}) == 2.5);
        assert(solution.findMedianSortedArrays(new int[]{1}, new int[]{2,3}) == 2.0);
        assert(solution.findMedianSortedArrays(new int[]{1}, new int[]{11,12,13,14}) == 12.0);
        assert(solution.findMedianSortedArrays(new int[]{3}, new int[]{1,2,4}) == 2.5);
        assert(solution.findMedianSortedArrays(new int[]{1,3}, new int[]{2}) == 2.0);
        assert(solution.findMedianSortedArrays(new int[]{1, 2}, new int[]{3, 4}) == 2.5);
        assert(solution.findMedianSortedArrays(new int[]{1,2,3,4,5,6,7,8,9}, new int[]{500}) == 5.5);
    }

}