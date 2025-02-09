package sudoku;

import java.util.ArrayList;

import solvers.Solver;
import solvers.backtrack.BacktrackOptimized;
import solvers.backtrack.Backtrack;
import solvers.wfc.WaveFunctionCollapse;

public class GenerateSudoku {

    private Grid grid;
    private double percentage;
    private Solver solver;

    public GenerateSudoku(Grid grid, double percentage) {
        this.grid = grid;
        this.percentage = percentage;
    }

    public enum SolverType {
        WFC,
        BACKTRACK,
        BACKTRACK_OPTIMIZED,
    }

    public void generateSudoku(SolverType solverType) {
        switch (solverType) {
            case WFC:
                solver = new WaveFunctionCollapse(grid);
                break;
            case BACKTRACK:
                solver = new Backtrack(grid);
                break;
            case BACKTRACK_OPTIMIZED:
                solver = new BacktrackOptimized(grid);
                break;
            default:
                throw new IllegalArgumentException("Unknown solver type");
        }
        long startTime = System.currentTimeMillis();
        solver.solve();
        long endTime = System.currentTimeMillis();
        System.out.println("Total solve time: " + (endTime - startTime) + "ms");
        deleteRandomCells((int) (grid.getSize().getX() * grid.getSize().getY() * this.percentage));
        solver.getGrid().print();
    }

    public void deleteRandomCells(int nbCells) {
        System.out.println("Deleting " + nbCells + " cells");
        ArrayList<Position> positions = new ArrayList<>();
        for (int x = 0; x < grid.getSize().getX(); x++) {
            for (int y = 0; y < grid.getSize().getY(); y++) {
                if (solver.getGrid().getCell(new Position(x, y)) != null
                        && solver.getGrid().getSymbol(new Position(x, y)) != null)
                    positions.add(new Position(x, y));
            }
        }
        if (positions.isEmpty()) {
            System.out.println("No positions available to delete.");
            return;
        }
        while (nbCells > 0 && !positions.isEmpty()) {
            int id = (int) (Math.random() * positions.size());
            Position position = positions.remove(id);
            Cell cell = solver.getGrid().getCell(position);
            if (cell != null && cell.getSymbol() != null) {
                String symbol = cell.getSymbol();
                solver.getGrid().resetSymbol(position);

                int nbSolution = solver.getNumberOfSolutions();

                if (nbSolution > 1) {
                    System.out.println("Multiple solutions");
                    solver.getGrid().insertSymbol(symbol, position);
                } else {
                    System.out.println("Deleting cell " + position + " with symbol " + symbol);
                    nbCells--;
                }
            }
        }

    }

    public Grid getGrid() {
        return grid;
    }
}
