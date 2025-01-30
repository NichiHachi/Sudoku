package sudoku;

import java.util.*;

public class Main {
    public static void main(String[] args){
        Sudoku sudoku0 = new Sudoku(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"});

        ArrayList<Sudoku> sudokus = new ArrayList<>();
        sudokus.add(sudoku0);

        Grid grid = new Grid(sudokus);
        grid.playTerminal();
    }
}
