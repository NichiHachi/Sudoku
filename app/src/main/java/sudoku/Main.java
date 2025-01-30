package sudoku;

import solvers.wfc.WaveFunctionCollapse;

import java.util.*;

import sudoku.graphic.MainGraphic;

public class Main {
    public static void main(String[] args) {
        Sudoku sudoku1 = new Sudoku(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" });
        Sudoku sudoku2 = new Sudoku(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" }, 1);
        Sudoku sudoku3 = new Sudoku(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" }, -1);

        ArrayList<Sudoku> sudokus = new ArrayList<>();
        sudokus.add(sudoku1);
//        sudokus.add(sudoku2);
//        sudokus.add(sudoku3);

        Grid grid = new Grid(sudokus);
        // grid.playTerminal();

    }
}
