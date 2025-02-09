package sudoku;

import sudoku.sudoku.SudokuClassic;
import sudoku.terminal.PlayTerminal;

public class Main {
    public static void main(String[] args) {
        Grid grid = new Grid.Builder()
                .addSudoku(new SudokuClassic(2))
                .build();

        GenerateSudoku sudokuGenerator = new GenerateSudoku(grid, 0);

        sudokuGenerator.generateSudoku(GenerateSudoku.SolverType.WFC);

        PlayTerminal playTerminal = new PlayTerminal();
        playTerminal.start();        // Grid grid2 =
        // SudokuImporter.importFromFile("./src/main/java/sudokuSaved/sudoku2*2.txt");
        // grid2.print();
    }
}
