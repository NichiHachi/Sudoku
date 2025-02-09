package solvers.backtrack;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solvers.Solver;
import sudoku.Cell;
import sudoku.Grid;
import sudoku.Position;
import utils.Colors;

/**
 * Implements a Backtracking solver for Sudoku puzzles.
 * <p>
 * This class extends the {@link Solver} class and utilizes a recursive backtracking algorithm
 * to find a solution for a given Sudoku grid. It attempts to fill empty cells with valid
 * symbols, backtracking when a dead end is reached.
 */
public class Backtrack extends Solver {

    private static final Logger logger = LoggerFactory.getLogger(
        Backtrack.class
    );
    private int attempts = 0;

    /**
     * Constructs a {@code Backtrack} solver for the given Sudoku grid.
     *
     * @param grid The Sudoku grid to be solved.
     * @see Solver#Solver(Grid)
     */
    public Backtrack(Grid grid) {
        super(grid);
        logger.info(
            Colors.GREEN + "Starting Backtrack solver..." + Colors.RESET
        );
    }

    /**
     * Solves the Sudoku grid using the backtracking algorithm.
     * <p>
     * This method initiates the backtracking process by calling the recursive {@link #backtrack(int, int)}
     * method starting from the top-left cell (row 0, column 0).
     * It logs the start and end of the backtracking process using SLF4j logger.
     *
     * @see #backtrack(int, int)
     */
    @Override
    public void solve() {
        logger.info(
            Colors.INFO_COLOR +
            "Starting backtracking process..." +
            Colors.RESET
        );
        backtrack(0, 0);
    }

    @Override
    public int getNumberOfSolutions() {
        return -1;
    }

    /**
     * Recursively attempts to solve the Sudoku grid using backtracking.
     * <p>
     * This is the core method of the backtracking algorithm. It tries to fill the cell at the given
     * {@code row} and {@code col} with a valid symbol. If successful, it recursively calls itself
     * for the next cell. If it reaches a dead end or finds a conflict, it backtracks by resetting
     * the current cell and trying the next possible symbol.
     *
     * @param row The current row index to process.
     * @param col The current column index to process.
     * @return {@code true} if a solution is found from this point, {@code false} otherwise.
     *         Returns {@code true} when the entire grid is filled successfully (base case).
     */
    private boolean backtrack(int row, int col) {
        attempts++;

        Position size = grid.getSize();

        // Backtracking is done.
        if (row >= size.getY()) {
            logger.info(
                Colors.SUCCESS_COLOR +
                "Solution found after " +
                attempts +
                " attempts!" +
                Colors.RESET
            );
            return true;
        }

        //
        int nextRow = (col + 1 >= size.getX()) ? row + 1 : row;
        int nextCol = (col + 1 >= size.getX()) ? 0 : col + 1;

        Position currentPos = new Position(col, row);
        Cell currentCell = grid.getCell(currentPos);

        if (currentCell == null || currentCell.getSymbol() != null) {
            return backtrack(nextRow, nextCol);
        }

        Set<String> possibleValues = new HashSet<>(
            grid.getPossiblePlays(currentPos)
        );

        for (String value : possibleValues) {
            grid.insertSymbol(value, currentPos);

            logger.debug(
                Colors.INFO_COLOR +
                "Attempt #" +
                attempts +
                " at position (" +
                row +
                "," +
                col +
                ") with value " +
                Colors.HIGHLIGHT_COLOR +
                value +
                Colors.RESET
            );
            if (backtrack(nextRow, nextCol)) {
                return true;
            }
            logger.debug(
                Colors.WARNING_COLOR +
                "Rolling back from position (" +
                row +
                "," +
                col +
                ") | Value: " +
                value +
                Colors.RESET
            );
            currentCell.resetSymbol();
        }

        return false;
    }
}
