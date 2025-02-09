package sudoku.graphic;

import sudoku.Grid;
import sudoku.Position;
import sudoku.sudoku.SudokuClassic;

public class MainGraphic {

    public static void main(String[] args) {
        Grid grid = new Grid.Builder()
                .addSudoku(new SudokuClassic(16, new Position(-12, 0)))
                .addSudoku(new SudokuClassic(16, new Position(0, -12)))
                .addSudoku(new SudokuClassic(16, new Position(12, 0)))
                .addSudoku(new SudokuClassic(16, new Position(0, 12)))
                .build();
        // grid.print();
        // GenerateSudoku sudokuGenerator = new GenerateSudoku(grid, 0.9);

        // sudokuGenerator.generateSudoku(GenerateSudoku.SolverType.BACKTRACK_OPTIMIZED);

        // grid = sudokuGenerator.getGrid();

        // Grid grid2 =
        // SudokuImporter.importFromFile("./src/main/java/sudokuSaved/sudoku4*4.txt");
        // GenerateSudoku sudokuGenerator2 = new GenerateSudoku(grid2, 0.7);
        // sudokuGenerator2.generateSudoku();
        // grid2 = sudokuGenerator2.getGrid();

        GridGraphic main = new GridGraphic(grid);
        main.init();
    }

}
