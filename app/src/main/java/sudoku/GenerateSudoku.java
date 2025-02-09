package sudoku;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solvers.Solver;
import solvers.backtrack.Backtrack;
import solvers.backtrack.BacktrackOptimized;
import solvers.wfc.WaveFunctionCollapse;

public class GenerateSudoku {

    private static final Logger logger = LoggerFactory.getLogger(
        GenerateSudoku.class
    );
    private final Grid grid;
    private final double percentage;
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
        solver = switch (solverType) {
            case WFC -> new WaveFunctionCollapse(grid);
            case BACKTRACK -> new Backtrack(grid);
            case BACKTRACK_OPTIMIZED -> new BacktrackOptimized(grid);
            default -> throw new IllegalArgumentException(
                "Unknown solver type"
            );
        };
        long startTime = System.currentTimeMillis();
        solver.solve();
        long endTime = System.currentTimeMillis();
        logger.info("Total solve time: " + (endTime - startTime) + "ms");
        deleteRandomCells((int) (grid.getNbOfCellNotNull() * this.percentage));
    }

    public void deleteRandomCells(int nbCells) {
        logger.info("Deleting " + nbCells + " cells");
        ArrayList<Position> positions = new ArrayList<>();
        for (int x = 0; x < grid.getSize().getX(); x++) {
            for (int y = 0; y < grid.getSize().getY(); y++) {
                if (
                    solver.getGrid().getCell(new Position(x, y)) != null &&
                    solver.getGrid().getSymbol(new Position(x, y)) != null
                ) positions.add(new Position(x, y));
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
                    logger.debug(
                        "Deleting cell " + position + " with symbol " + symbol
                    );
                    nbCells--;
                }
            }
        }

    }

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

    public Grid getGrid() {
        return grid;
    }
}
