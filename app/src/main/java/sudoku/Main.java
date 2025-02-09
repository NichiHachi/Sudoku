package sudoku;

import solvers.Solver;
import solvers.backtrack.BacktrackOptimized;
import sudoku.sudoku.SudokuClassic;

public class Main {
    public static void main(String[] args) {
        Grid grid = new Grid.Builder()
                .addSudoku(new SudokuClassic(9, new Position(0, 0), true))
                .build();
        grid.print();

        GenerateSudoku sudokuGenerator = new GenerateSudoku(grid, 0.5);
        sudokuGenerator.generateSudoku(GenerateSudoku.SolverType.BACKTRACK_OPTIMIZED);

        grid = sudokuGenerator.getGrid();

        Solver solver = new BacktrackOptimized(grid);
        solver.solve();
        solver.getGrid().print();

    }
}
