package sudoku;

import solvers.Solver;
import solvers.wfc.WaveFunctionCollapse;

public class GenerateSudoku {

    private Grid grid;

    public GenerateSudoku(Grid grid) {
        this.grid = grid;
    }

    public void generateSudoku() {
        Solver solver = new WaveFunctionCollapse(grid);
        solver.solve();
        deleteRandomCells((int) (grid.getSize().getX() * grid.getSize().getY() * 0.75));
        grid.print();
    }

    public void deleteRandomCells(int nbCells) {
        int nbCellsDeleted = 0;
        System.out.println("Deleting " + nbCells + " cells");
        while (nbCellsDeleted < nbCells) {
            int x = (int) (Math.random() * grid.getSize().getX());
            int y = (int) (Math.random() * grid.getSize().getY());
            Cell cell = grid.getCell(new Position(x, y));
            if (cell.getSymbol() != null) {
                if (1 == 1) {
                    grid.setCell(new Position(x, y), new Cell());
                    nbCellsDeleted++;
                }
            }
        }
    }
}
