package sudoku;

import solvers.Solver;
import solvers.wfc.WaveFunctionCollapse;
import sudoku.sudoku.Sudoku;
import sudoku.sudoku.SudokuClassic;

import java.util.*;

public class Main {
    public static void main(String[] args){
        Grid grid = new Grid.Builder()
                .addSudoku(new SudokuClassic(9))
                .build();

        Solver solver = new WaveFunctionCollapse(grid);
        solver.solve();
        grid.print();
    }
}
