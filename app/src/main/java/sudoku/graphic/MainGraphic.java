package sudoku.graphic;

import sudoku.GenerateSudoku;
import sudoku.Grid;
import sudoku.Position;
import sudoku.sudoku.SudokuClassic;
import sudoku.terminal.PlayTerminal;

/**
 * The MainGraphic class serves as the entry point for the graphical Sudoku
 * application.
 */
public class MainGraphic {

    /**
     * The main method that starts the graphical interface for generating and
     * displaying Sudoku puzzles.
     *
     * @param args the command-line arguments
     */
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
                GenerateSudoku.SolverType.BACKTRACK_OPTIMIZED);

        grid = sudokuGenerator.getGrid();

        GridGraphic main = new GridGraphic(grid);
        main.init();
    }
}