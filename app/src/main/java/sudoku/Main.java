package sudoku;

import sudoku.sudoku.SudokuClassic;

public class Main {
    public static void main(String[] args) {
        Grid grid = new Grid.Builder()
                .addSudoku(new SudokuClassic(6))
                .build();

        GenerateSudoku sudokuGenerator = new GenerateSudoku(grid, 0.90);

        sudokuGenerator.generateSudoku();

    }
}
