package sudoku.graphic;

import sudoku.GenerateSudoku;
import sudoku.Grid;
import sudoku.Position;
import sudoku.sudoku.SudokuClassic;
import sudoku.terminal.PlayTerminal;

public class MainGraphic {

    public static void main(String[] args) {
        PlayTerminal.configureLogging(args);

        Grid grid = new Grid.Builder()
            .addSudoku(new SudokuClassic(9, new Position(-8, 0)))
            .addSudoku(new SudokuClassic(9, new Position(0, -8)))
            .addSudoku(new SudokuClassic(9, new Position(8, 0)))
            .addSudoku(new SudokuClassic(9, new Position(0, 8)))
            .build();

        GenerateSudoku sudokuGenerator = new GenerateSudoku(grid, 0.5);

        sudokuGenerator.generateSudoku(
            GenerateSudoku.SolverType.BACKTRACK_OPTIMIZED
        );

        grid = sudokuGenerator.getGrid();

        grid.print();

        // Grid grid2 =
        // SudokuImporter.importFromFile("./src/main/java/sudokuSaved/sudoku4*4.txt");
        // GenerateSudoku sudokuGenerator2 = new GenerateSudoku(grid2, 0.7);
        // sudokuGenerator2.generateSudoku();
        // grid2 = sudokuGenerator2.getGrid();

        GridGraphic main = new GridGraphic(grid);
        main.init();
    }
}
