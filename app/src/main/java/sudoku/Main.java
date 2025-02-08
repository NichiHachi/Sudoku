package sudoku;

import sudoku.sudoku.SudokuClassic;

public class Main {
    public static void main(String[] args) {
        // PlayTerminal playTerminal = new PlayTerminal();
        // playTerminal.start();

        Grid grid = new Grid.Builder().addSudoku(new SudokuClassic(9, new Position(-3, 0)))
                .addSudoku(new SudokuClassic(9, new Position(0, 3))).build();

        GenerateSudoku generator = new GenerateSudoku(grid, 0);
        generator.generateSudoku();
        grid = generator.getGrid();
        SudokuSaver.save(grid, "sudoku.txt");

        Grid grid2 = SudokuImporter.importFromFile("sudoku.txt");
        grid2.print();
    }
}
