import java.util.*
import kotlin.collections.HashMap

/**
 * Sudoku Solver
 * The solution involves keeping track of `impossibilities` for each grid index.
 * Impossibilities are values that a grid index can't have due to the same value being present on a row/column/square.
 * If there are 8 impossibilities, we know there is a unique solution for that grid index.
 * Otherwise, if there are less than 8 impossibilities we must save the gamestate and take a risk at picking one
 * of the possible values for that grid index. Then later on, if at any point, there is a grid index with
 * 9 impossibilities we know there can be no viable solution, so we backtrack to the last game state we saved and
 * continue from then, making sure to mark the 'trial' value as an impossibility too.
 *
 * This guarantees we reach a solution if one exists.
 */
class Solution {

    companion object {
        private const val EMPTY_CHAR = '.'
    }

    data class GridIndex(val row: Int, val col: Int) {
        companion object {
            fun of(row: Int, col: Int) = GridIndex(row, col)
        }
    }

    data class GameState(
        val board: Array<CharArray>,
        val impossibilities: HashMap<GridIndex, MutableSet<Char>>,
        val index: GridIndex,
        val trial: Char
    )

    fun solveSudoku(board: Array<CharArray>) {
        val trialStack = Stack<GameState>()
        var impossibilities = HashMap<GridIndex, MutableSet<Char>>()

        fun updateRowImpossibilities(row: Int, impossibilitiesSet: MutableSet<Char>) {
            for (i in 0 until 9) {
                if (board[row][i] == EMPTY_CHAR) {
                    impossibilities.merge(GridIndex.of(row, i), impossibilitiesSet) { prev, next -> (prev + next).toMutableSet() }
                }
            }
        }

        fun updateRowImpossibilities(row: Int) {
            val seen = mutableSetOf<Char>()
            for (i in 0 until 9) {
                val currentValue = board[row][i]
                if (currentValue != EMPTY_CHAR) {
                    seen.add(currentValue)
                }
            }
            updateRowImpossibilities(row, seen)
        }

        fun updateRowImpossibilities(row: Int, value: Char) = updateRowImpossibilities(row, mutableSetOf(value))

        fun updateColumnImpossibilities(col: Int, impossibilitiesSet: MutableSet<Char>) {
            for (i in 0 until 9) {
                val currentValue = board[i][col]
                if (currentValue == EMPTY_CHAR) {
                    impossibilities.merge(GridIndex.of(i, col), impossibilitiesSet) { prev, next -> (prev + next).toMutableSet() }
                }
            }
        }

        fun updateColumnImpossibilities(col: Int) {
            val seen = mutableSetOf<Char>()
            for (i in 0 until 9) {
                val currentValue = board[i][col]
                if (currentValue != EMPTY_CHAR) {
                    seen.add(currentValue)
                }
            }
            updateColumnImpossibilities(col, seen)
        }

        fun updateColumnImpossibilities(col: Int, value: Char) = updateColumnImpossibilities(col, mutableSetOf(value))

        fun updateSquareImpossibilities(gridIndex: GridIndex, impossibilitiesSet: MutableSet<Char>) {
            val rowOffset = (gridIndex.row / 3) * 3
            val colOffset = (gridIndex.col / 3) * 3

            for (i in 0 until 3) {
                for (j in 0 until 3) {
                    if (board[rowOffset + i][colOffset + j] == EMPTY_CHAR) {
                        impossibilities.merge(GridIndex.of(rowOffset + i, colOffset + j), impossibilitiesSet) { prev, next -> (prev + next).toMutableSet() }
                    }
                }
            }
        }

        fun updateSquareImpossibilities(gridIndex: GridIndex) {
            val seen = mutableSetOf<Char>()
            val rowOffset = (gridIndex.row / 3) * 3
            val colOffset = (gridIndex.col / 3) * 3

            for (i in 0 until 3) {
                seen.add(board[rowOffset + 0][colOffset + i])
                seen.add(board[rowOffset + 1][colOffset + i])
                seen.add(board[rowOffset + 2][colOffset + i])
            }
            seen.remove(EMPTY_CHAR)
            updateSquareImpossibilities(gridIndex, seen)
        }

        fun updateSquareImpossibilities(gridIndex: GridIndex, value: Char) = updateSquareImpossibilities(gridIndex, mutableSetOf(value))

        fun validateOrBacktrack(): Boolean {
            if (impossibilities.filter { it.value.count() == 9 }.isNotEmpty()) {
                // We messed up during a trial, we must backtrack
                val prevGameState = trialStack.pop()
                for (i in 0 until 9) {
                    board[i] = prevGameState.board[i]
                }

                impossibilities = prevGameState.impossibilities
                impossibilities.merge(prevGameState.index, mutableSetOf(prevGameState.trial)) { prev, next -> (prev + next).toMutableSet() }
                return false
            }
            return true
        }

        fun populate(gridIndex: GridIndex, value: Char) {
            // Update board
            board[gridIndex.row][gridIndex.col] = value

            // Remove this item as it has been solved
            impossibilities.remove(gridIndex)

            // Update impossibilities
            updateRowImpossibilities(gridIndex.row, value)
            updateColumnImpossibilities(gridIndex.col, value)
            updateSquareImpossibilities(gridIndex, value)
        }

        // Initial state
        for (i in 0 until 9) {
            updateRowImpossibilities(i)
            updateColumnImpossibilities(i)
        }

        for (i in 0 until 3) {
            for (j in 0 until 3) {
                updateSquareImpossibilities(GridIndex.of(i*3, j*3))
            }
        }

        while (impossibilities.isNotEmpty()) {
            val filterResults = impossibilities.filter { it.value.count() == 8 }
            if (filterResults.isNotEmpty()) {
                run filterLoop@{
                    filterResults.forEach {
                        val solution = getPossibleChar(it.value).first()
                        populate(it.key, solution)

                        // Optimisation: Continue processing all valid solutions until we backtrack
                        if (!validateOrBacktrack()) {
                            return@filterLoop
                        }
                    }
                }
            } else {
                // There is no guaranteed solution, we must take a risk and backtrack later if needed
                val nextTrial = impossibilities.maxBy { it.value.count() }
                val solution = getPossibleChar(nextTrial?.value!!).first()

                // Add new trial onto stack
                trialStack.add(
                    GameState(
                        board.map { it.copyOf() }.toTypedArray(),
                        HashMap(impossibilities),
                        nextTrial.key,
                        solution
                    )
                )

                // Apply solution
                populate(nextTrial.key, solution)
                validateOrBacktrack()
            }
        }

    }

    private fun getPossibleChar(seen: Set<Char>): Set<Char> {
        val possibilities = ('1' .. '9').toMutableSet()
        return possibilities.minus(seen)
    }
}
