package sudoku;

import sudoku.sudoku.SudokuClassic;

public class Main {
    public static void main(String[] args) {
        Grid grid = new Grid.Builder()
                .addSudoku(new SudokuClassic(2))
                .build();

        GenerateSudoku sudokuGenerator = new GenerateSudoku(grid, 0);

        sudokuGenerator.generateSudoku(GenerateSudoku.SolverType.WFC);

        for (int i = 2; i <= 10; i++) {
            grid = new Grid.Builder()
                    .addSudoku(new SudokuClassic(i))
                    .build();

            sudokuGenerator = new GenerateSudoku(grid, 0);

            sudokuGenerator.generateSudoku(GenerateSudoku.SolverType.WFC);

            SudokuSaver.save(grid, "./src/main/java/sudokuSaved/sudoku" + i + "*" + i + ".txt");
        }

        Grid grid2 = SudokuImporter.importFromFile("./src/main/java/sudokuSaved/sudoku2*2.txt");
        grid2.print();
    }
}
