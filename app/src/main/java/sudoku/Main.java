package sudoku;

import solvers.wfc.WaveFunctionCollapse;

import java.util.*;

public class Main {
    public static void main(String[] args){
        Sudoku sudoku1 = new Sudoku(new String[]{"1", "2", "3", "4"});
        Sudoku sudoku2 = new Sudoku(new String[]{"1", "2", "3", "4"}, 1);

        ArrayList<Sudoku> sudokus = new ArrayList<>();
        sudokus.add(sudoku1);
//        sudokus.add(sudoku2);
//        sudokus.add(sudoku3);

        Grid grid = new Grid(sudokus);
        WaveFunctionCollapse solver = new WaveFunctionCollapse(grid);
        solver.solve();
    }
}
