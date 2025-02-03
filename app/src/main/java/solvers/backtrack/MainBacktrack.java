package solvers.backtrack;


import sudoku.Grid;
import sudoku.Sudoku;

import java.util.ArrayList;

public class MainBacktrack {
    public static void main(String[] args) {
        System.out.println("Running Backtrack");
        Sudoku sudoku0 = new Sudoku(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"});

        ArrayList<Sudoku> sudokus = new ArrayList<>();
        sudokus.add(sudoku0);

        Grid grid = new Grid(sudokus);
        Backtrack solver = new Backtrack(grid);
        solver.solve();
        System.out.println("Done!");
    }
}
