package solvers.backtrack;

import java.util.HashSet;
import java.util.Set;

import solvers.Solver;
import sudoku.Cell;
import sudoku.Grid;
import sudoku.Position;

public class Backtrack extends Solver {
    private int attempts = 0;

    public Backtrack(Grid grid) {
        super(grid);
    }

    @Override
    public void solve() {
        System.out.println("Backtracking");
        backtrack(0, 0);
        // grid.print();
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

        Set<String> possibleValues = new HashSet<>(grid.getPossiblePlays(currentPos));

        for (String value : possibleValues) {
            grid.insertSymbol(value, currentPos);

            System.out.println("Attempts: " + attempts + " at " + row + " " + col + " with " + value);
            if (backtrack(nextRow, nextCol)) {
                return true;
            }

            currentCell.resetSymbol();
        }

        return false;
    }
}
