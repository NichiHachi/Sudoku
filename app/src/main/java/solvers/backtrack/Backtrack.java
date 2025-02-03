package solvers.backtrack;

import java.util.HashSet;
import java.util.Set;

import solvers.Solver;
import sudoku.Cell;
import sudoku.Grid;
import sudoku.Position;

public class Backtrack extends Solver {
    public Backtrack(Grid grid) {
        super(grid);
    }

    private int attempts = 0;

    @Override
    public void solve() {
        backtrack(0, 0);
        grid.print();
    }

    private boolean backtrack(int row, int col) {
        attempts++;
        System.out.println("Attempts: " + attempts + " at " + row + " " + col);

        Position size = grid.getSize();

        if (row >= size.getY()) {
            return true;
        }

        int nextRow = (col + 1 >= size.getX()) ? row + 1 : row;
        int nextCol = (col + 1 >= size.getX()) ? 0 : col + 1;

        Position currentPos = new Position(col, row);
        Cell currentCell = grid.getCell(currentPos);

        if (currentCell == null || currentCell.getValue() != null) {
            return backtrack(nextRow, nextCol);
        }

        Set<String> possibleValues = new HashSet<>(grid.getPossiblePlays(currentPos));
        System.out.println("Possible values at " + currentPos + ": " + possibleValues.size() + " " + possibleValues);

        for (String value : possibleValues) {
            grid.insertValue(value, currentPos);
            System.out.println("Choseen value: " + value + " at " + currentPos);
            grid.print();
            System.out.println("--- Next: " + nextRow + " " + nextCol);

            if (backtrack(nextRow, nextCol)) {
                return true;
            }

            System.out.println("Reseting value at " + currentPos);
            currentCell.resetValue();
            grid.print();
            System.out.println("---end of reset ---");
        }

        return false;
    }
}
