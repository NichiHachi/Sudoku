package sudoku;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Sudoku sudoku1 = new Sudoku(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" });
        Sudoku sudoku2 = new Sudoku(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" }, 1);
        Sudoku sudoku3 = new Sudoku(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" }, -1);

        ArrayList<Sudoku> sudokus = new ArrayList<>();
        sudokus.add(sudoku1);

        Grid grid = new Grid(sudokus);
        // grid.playTerminal();

    }
}
