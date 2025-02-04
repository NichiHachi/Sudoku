package sudoku;

import solvers.Solver;
import solvers.wfc.WaveFunctionCollapse;
import sudoku.sudoku.Sudoku;
import sudoku.sudoku.SudokuClassic;

import java.util.*;

public class Main {
    public static void main(String[] args){
        Sudoku sudoku0 = new SudokuClassic(6);
        Sudoku sudoku1 = new SudokuClassic(6, 1);
        Sudoku sudoku2 = new SudokuClassic(6, 2);

        Grid grid = new Grid.Builder()
                .addSudoku(sudoku0)
                .addSudoku(sudoku1)
                .addSudoku(sudoku2)
                .build();

        Solver solver = new WaveFunctionCollapse(grid);
        solver.solve();
        grid.print();
    }
}
