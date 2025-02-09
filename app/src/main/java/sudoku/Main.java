package sudoku;

import sudoku.configuration.SudokuSaver;
import sudoku.sudoku.SudokuClassic;

public class Main {
    public static void main(String[] args) {
        Grid grid = new Grid.Builder()
                .addSudoku(new SudokuClassic(2))
                .build();

        GenerateSudoku sudokuGenerator = new GenerateSudoku(grid, 0);
        for (int i = 2; i <= 20; i++) {
            grid = new Grid.Builder()
                    .addSudoku(new SudokuClassic(i))
                    .build();
            sudokuGenerator = new GenerateSudoku(grid, 0);
            sudokuGenerator.generateSudoku(GenerateSudoku.SolverType.BACKTRACK_OPTIMIZED);
            grid = sudokuGenerator.getGrid();
            SudokuSaver.save(grid, "./src/main/java/sudokuSaved/sudoku" + grid.getSize().getX() + "par"
                    + grid.getSize().getY() + ".txt");

        }
    }
}
