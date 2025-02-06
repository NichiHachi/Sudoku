package sudoku;

import solvers.Solver;
import solvers.wfc.WaveFunctionCollapse;

public class GenerateSudoku {

    private Grid grid;
    private double percentage;

    public GenerateSudoku(Grid grid, double percentage) {
        this.grid = grid;
        this.percentage = percentage;
    }

    public void generateSudoku() {
        Solver solver = new WaveFunctionCollapse(grid);
        solver.solve();
        deleteRandomCells((int) (grid.getSize().getX() * grid.getSize().getY() * this.percentage));
        grid.print();
    }

    public void deleteRandomCells(int nbCells) {
        int nbCellsDeleted = 0;
        System.out.println("Deleting " + nbCells + " cells");
        while (nbCellsDeleted < nbCells) {
            int x = (int) (Math.random() * grid.getSize().getX());
            int y = (int) (Math.random() * grid.getSize().getY());
            Cell cell = grid.getCell(new Position(x, y));
            if (cell != null && cell.getSymbol() != null) {
                if (1 == 1) {
                    grid.resetSymbol(new Position(x, y));

                    nbCellsDeleted++;
                }
            }
        }
    }

    public Grid getGrid() {
        return grid;
    }
}
