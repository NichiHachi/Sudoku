package sudoku;

import sudoku.sudoku.Sudoku;
import sudoku.sudoku.SudokuClassic;

import java.util.*;

public class Main {
    public static void main(String[] args){
        Sudoku sudoku0 = new SudokuClassic(2);

        Grid grid = new Grid.Builder()
                .addSudoku(sudoku0)
                .build();

        grid.playTerminal();
    }
}
