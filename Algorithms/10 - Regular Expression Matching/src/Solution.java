class Solution {

    public static void main(String[] args)
    {
        test();
    }

    public static boolean isMatch(String s, String p) {
        int curStringCharIndex = 0;
        int curPatternCharIndex = 0;
        int nextStarIndex = findNextStar(p, 0);

        while (curPatternCharIndex < p.length()) {

            // Star (Zero or More) case
            if (curPatternCharIndex + 1 == nextStarIndex && nextStarIndex < p.length()) {
                while (curStringCharIndex < s.length() && compareChars(s.charAt(curStringCharIndex), p.charAt(curPatternCharIndex)))
                    ++curStringCharIndex;

                // Increment index and find next star index
                curPatternCharIndex += 2;
                nextStarIndex = findNextStar(p, curPatternCharIndex);
            }
            // Dot (Any char match) case
            else if (p.charAt(curPatternCharIndex) == '.') {
                ++curStringCharIndex;
                ++curPatternCharIndex;
            }
            // Simple char comparison case
            else {
                if (curStringCharIndex >= s.length() ||  s.charAt(curStringCharIndex) != p.charAt(curPatternCharIndex))
                    return false;
                ++curStringCharIndex;
                ++curPatternCharIndex;
            }
        }

        return curStringCharIndex == s.length();
    }

    private static int findNextStar(String s, final int startIndex) {
        int index = startIndex;
        while (index < s.length() && s.charAt(index) != '*') {
            ++index;
        }
        return index;
    }

    private static boolean compareChars(char c1, char c2)
    {
        if (c1 == '.' || c2 == '.')
            return true;
        else
            return c1 == c2;
    }

    private static void test()
    {
        // Simple pattern cases
        assert(Solution.isMatch("a", "a"));
        assert(Solution.isMatch("aabb", "aabb"));
        assert(Solution.isMatch("aabbccdd", "aabbccdd"));
        assert(!Solution.isMatch("aaaa", "aaaabbbb"));
        assert(!Solution.isMatch("aaaaaaaa", "aaaa"));

        // Dot Case
        assert(Solution.isMatch("aabbccdd", "a.bb.cd."));
        assert(Solution.isMatch("aaaa", "...."));
        assert(!Solution.isMatch("aaaa", "....."));

        // Star case
        assert(Solution.isMatch("a", "a*"));
        assert(Solution.isMatch("aaaa", "a*"));
        assert(!Solution.isMatch("aaaabbbb", "a*"));
        assert(Solution.isMatch("aaaabbbb", "a*b*"));

        // Complex
        assert(Solution.isMatch("a", ".*"));
        assert(Solution.isMatch("abc", ".*"));
        assert(Solution.isMatch("coolbeanstoday", ".*beans.*"));
    }
}