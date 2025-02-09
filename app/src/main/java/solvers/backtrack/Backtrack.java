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

public class Backtrack extends Solver {

    private static final Logger logger = LoggerFactory.getLogger(
        Backtrack.class
    );
    private int attempts = 0;

    public Backtrack(Grid grid) {
        super(grid);
        logger.info(
            Colors.GREEN + "Starting Backtrack solver..." + Colors.RESET
        );
    }

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
