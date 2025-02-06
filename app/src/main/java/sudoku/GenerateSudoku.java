package sudoku;

import java.util.ArrayList;

import solvers.Solver;
import solvers.wfc.WaveFunctionCollapse;

public class GenerateSudoku {

    private Grid grid;
    private double percentage;
    private Solver solver;

    public GenerateSudoku(Grid grid, double percentage) {
        this.grid = grid;
        this.percentage = percentage;
    }

    public void generateSudoku() throws CloneNotSupportedException {
        solver = new WaveFunctionCollapse(grid);
        solver.solve();
        deleteRandomCells((int) (grid.getSize().getX() * grid.getSize().getY() * this.percentage));
        solver.getGrid().print();
    }

    public void deleteRandomCells(int nbCells) throws CloneNotSupportedException {
        System.out.println("Deleting " + nbCells + " cells");
        ArrayList<Position> positions = new ArrayList<>();
        for (int x = 0; x < grid.getSize().getX(); x++) {
            for (int y = 0; y < grid.getSize().getY(); y++) {
                if (solver.getGrid().getCell(new Position(x, y)) != null
                        && solver.getGrid().getSymbol(new Position(x, y)) != null)
                    positions.add(new Position(x, y));
            }
        }
        while (nbCells > 0 || positions.isEmpty()) {
            int id = (int) (Math.random() * positions.size());
            Position position = positions.remove(id);
            Cell cell = solver.getGrid().getCell(position);
            if (cell != null && cell.getSymbol() != null) {
                String symbol = cell.getSymbol();
                solver.getGrid().resetSymbol(position);
                int nbSolution = 0;
                try {
                    nbSolution = solver.getNumberOfSolutions();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }

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
