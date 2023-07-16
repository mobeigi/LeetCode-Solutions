import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SolutionTest {

    @Test
    fun `test example 1`() {
        val allInOne = AllOne()

        allInOne.inc("hello")
        allInOne.inc("hello")
        assertEquals(expected = "hello", actual = allInOne.getMaxKey())
        assertEquals(expected = "hello", actual = allInOne.getMinKey())

        allInOne.inc("leet")
        assertEquals(expected = "hello", actual = allInOne.getMaxKey())
        assertEquals(expected = "leet", actual = allInOne.getMinKey())
    }

    @Test
    fun `test case 2`() {
        val allInOne = AllOne()
        assertEquals(expected = "", actual = allInOne.getMaxKey())
        assertEquals(expected = "", actual = allInOne.getMinKey())
    }

    @Test
    fun `test case 8`() {
        val allInOne = AllOne()

        allInOne.inc("hello")
        allInOne.inc("goodbye")
        allInOne.inc("hello")
        allInOne.inc("hello")
        assertEquals(expected = "hello", actual = allInOne.getMaxKey())

        allInOne.inc("leet")
        allInOne.inc("code")
        allInOne.inc("leet")
        allInOne.dec("hello")
        allInOne.inc("leet")
        allInOne.inc("code")
        allInOne.inc("code")
        assertEquals(expected = "leet", actual = allInOne.getMaxKey())
    }
}
