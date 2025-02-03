package solvers.backtrack;


import java.util.ArrayList;

import sudoku.Grid;
import sudoku.Sudoku;

public class MainBacktrack {
    public static void main(String[] args) {
        System.out.println("Running Backtrack");
        Sudoku sudoku0 = new Sudoku(new String[]{"1", "2", "3"});

        ArrayList<Sudoku> sudokus = new ArrayList<>();
        sudokus.add(sudoku0);

        Grid grid = new Grid(sudokus);
        Backtrack solver = new Backtrack(grid);
        solver.solve();
        System.out.println("Done!");
    }
}
