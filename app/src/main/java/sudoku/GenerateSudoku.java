package sudoku;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import solvers.Solver;
import solvers.backtrack.Backtrack;
import solvers.backtrack.BacktrackOptimized;
import solvers.wfc.WaveFunctionCollapse;

/**
 * This class is responsible for generating a Sudoku puzzle.
 * It uses different solving algorithms to generate a complete Sudoku grid
 * and then removes a certain percentage of cells to create the puzzle.
 */
public class GenerateSudoku {

    private static final Logger logger = LoggerFactory.getLogger(GenerateSudoku.class);
    private final Grid grid;
    private final double percentage;
    private Solver solver;

    /**
     * Constructs a GenerateSudoku instance with the specified grid and percentage
     * of cells to remove.
     *
     * @param grid       the initial Sudoku grid
     * @param percentage the percentage of cells to remove to create the puzzle
     */
    public GenerateSudoku(Grid grid, double percentage) {
        this.grid = grid;
        this.percentage = percentage;
    }

    /**
     * Enum representing the different types of solvers available.
     */
    public enum SolverType {
        WFC,
        BACKTRACK,
        BACKTRACK_OPTIMIZED,
    }

    /**
     * Generates a Sudoku puzzle using the specified solver type.
     *
     * @param solverType the type of solver to use for generating the Sudoku puzzle
     */
    public void generateSudoku(SolverType solverType) {
        solver = switch (solverType) {
            case WFC -> new WaveFunctionCollapse(grid);
            case BACKTRACK -> new Backtrack(grid);
            case BACKTRACK_OPTIMIZED -> new BacktrackOptimized(grid);
            default -> throw new IllegalArgumentException("Unknown solver type");
        };
        long startTime = System.currentTimeMillis();
        solver.solve();
        long endTime = System.currentTimeMillis();
        logger.info("Total solve time: " + (endTime - startTime) + "ms");
        if (solver.getGrid().isRandomBlock()) {
            switchRandomCase();
        }
        deleteRandomCells((int) (grid.getNbOfCellNotNull() * this.percentage));
    }

    /**
     * Deletes a specified number of cells randomly from the grid.
     *
     * @param nbCells the number of cells to delete
     */
    public void deleteRandomCells(int nbCells) {
        logger.info("Deleting " + nbCells + " cells");
        ArrayList<Position> positions = new ArrayList<>();
        for (int x = 0; x < grid.getSize().getX(); x++) {
            for (int y = 0; y < grid.getSize().getY(); y++) {
                if (solver.getGrid().getCell(new Position(x, y)) != null &&
                        solver.getGrid().getSymbol(new Position(x, y)) != null)
                    positions.add(new Position(x, y));
            }
        }
        if (positions.isEmpty()) {
            logger.warn("No positions available to delete.");
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
                    logger.debug("Multiple solutions");
                    solver.getGrid().insertSymbol(symbol, position);
                } else {
                    logger.debug("Deleting cell " + position + " with symbol " + symbol);
                    nbCells--;
                }
            }
        }
    }

    /**
     * Switches random cases in the grid to ensure randomness.
     */
    public void switchRandomCase() {
        System.out.println("Switching random cases");
        ArrayList<Position> switchedPositions = new ArrayList<>();
        System.out.println(grid.getSize().getX());
        System.out.println(grid.getSize().getY());
        for (int i = 0; i < grid.getSize().getX(); i++) {
            for (int j = 0; j < grid.getSize().getY(); j++) {

                Position position = new Position(i, j);
                if (switchedPositions.contains(position)) {
                    continue;
                }
                Position position2 = findCaseWithSameSymbol(position);
                if (position2 != null && !switchedPositions.contains(position2)) {
                    Cell cell = grid.getCell(position);
                    Cell cell2 = grid.getCell(position2);
                    if (cell != null && cell2 != null) {
                        String symbol = cell.getSymbol();
                        String symbol2 = cell2.getSymbol();
                        ArrayList<Integer> idRules = cell.getIdRules();
                        ArrayList<Integer> idRules2 = cell2.getIdRules();

                        for (int idRule : idRules) {
                            grid.getRule(idRule).getRulePositions().remove(position);
                            grid.getRule(idRule).getRulePositions().add(position2);
                        }
                        for (int idRule : idRules2) {
                            grid.getRule(idRule).getRulePositions().remove(position2);
                            grid.getRule(idRule).getRulePositions().add(position);
                        }
                        Cell newCell = new Cell(idRules2);
                        Cell newCell2 = new Cell(idRules);
                        newCell.insertSymbol(symbol);
                        newCell2.insertSymbol(symbol2);
                        grid.setCell(position, newCell);
                        grid.setCell(position2, newCell2);
                    }

                }

                switchedPositions.add(position);
            }
        }
    }

    /**
     * Finds a case with the same symbol as the given position.
     *
     * @param position the position to find a matching case for
     * @return a position with the same symbol, or null if none found
     */
    public Position findCaseWithSameSymbol(Position position) {
        String symbol = grid.getSymbol(position);
        ArrayList<Position> positions = new ArrayList<>();
        for (int i = 0; i < grid.getSize().getX(); i++) {
            for (int j = 0; j < grid.getSize().getY(); j++) {
                if (grid.getSymbol(new Position(i, j)) != null && grid.getSymbol(new Position(i, j)).equals(symbol)
                        && !position.equals(new Position(i, j))) {
                    positions.add(new Position(i, j));
                }
            }
        }
        if (positions.isEmpty()) {
            return null;
        }
        int randomIndex = (int) (Math.random() * positions.size());
        return positions.get(randomIndex);
    }

    /**
     * Gets the current Sudoku grid.
     *
     * @return the current grid
     */
    public Grid getGrid() {
        return grid;
    }
}