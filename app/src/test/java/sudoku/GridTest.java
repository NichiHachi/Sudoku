package sudoku;

import org.junit.jupiter.api.Test;
import sudoku.rule.Rule;
import sudoku.sudoku.SudokuClassic;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GridTest {

    @Test
    void testGridInitialization() {
        Grid grid = new Grid.Builder()
                .addSudoku(new SudokuClassic(2))
                .build();
        assertEquals(new Position(2, 2), grid.getSize());
    }

    @Test
    void testAddRule() {
        Grid grid = new Grid.Builder()
                .addSudoku(new SudokuClassic(2))
                .build();
        Rule rule = new Rule();
        rule.add(new Position(0, 0));
        grid.addRule(rule);
        assertEquals(5, grid.getRules().size());
    }

    @Test
    void testInsertSymbol() {
        Grid grid = new Grid.Builder()
                .addSudoku(new SudokuClassic(2))
                .build();
        grid.initCells();
        grid.insertSymbol("1", new Position(0, 0));
        assertEquals("1", grid.getSymbol(new Position(0, 0)));
    }

    @Test
    void testInsertSymbolOutOfRange() {
        Grid grid = new Grid.Builder()
                .addSudoku(new SudokuClassic(2))
                .build();
        grid.initCells();
        grid.insertSymbol("5", new Position(10, 10));
        assertNull(grid.getSymbol(new Position(10, 10)));
    }

    @Test
    void testResetSymbol() {
        Grid grid = new Grid.Builder()
                .addSudoku(new SudokuClassic(2))
                .build();
        grid.initCells();
        grid.insertSymbol("5", new Position(0, 0));
        grid.resetSymbol(new Position(0, 0));
        assertNull(grid.getSymbol(new Position(0, 0)));
    }

    @Test
    void testIsComplete() {
        Grid grid = new Grid.Builder()
                .addSudoku(new SudokuClassic(2))
                .build();
        grid.initCells();
        grid.insertSymbol("1", new Position(0, 0));
        grid.insertSymbol("2", new Position(0, 1));
        grid.insertSymbol("2", new Position(1, 0));
        grid.insertSymbol("1", new Position(1, 1));
        assertTrue(grid.isComplete());
    }

    @Test
    void testIsNotComplete() {
        Grid grid = new Grid.Builder()
                .addSudoku(new SudokuClassic(2))
                .build();
        grid.initCells();
        for (int y = 0; y < 1; y++) {
            for (int x = 0; x < 2; x++) {
                grid.insertSymbol("1", new Position(x, y));
            }
        }
        assertFalse(grid.isComplete());
    }

    @Test
    void testGetPossiblePlays() {
        Set<String> symbols = new HashSet<>();
        for (int i = 1; i <= 2; i++) {
            symbols.add(Integer.toString(i));
        }
        Grid grid = new Grid.Builder()
                .addSudoku(new SudokuClassic(2, symbols))
                .build();
        Set<String> possiblePlays = grid.getPossiblePlays(new Position(0, 0));
        assertEquals(symbols, possiblePlays);
    }

    @Test
    void testCountPossiblePlays() {
        Set<String> symbols = new HashSet<>();
        for (int i = 1; i <= 2; i++) {
            symbols.add(Integer.toString(i));
        }
        Grid grid = new Grid.Builder()
                .addSudoku(new SudokuClassic(2, symbols))
                .build();
        int count = grid.countPossiblePlays(new Position(0, 0));
        assertEquals(2, count);
    }

    @Test
    void testMergeSudokus() {
        Set<String> symbols = new HashSet<>();
        for (int i = 1; i <= 2; i++) {
            symbols.add(Integer.toString(i));
        }
        SudokuClassic sudoku1 = new SudokuClassic(2, symbols);
        SudokuClassic sudoku2 = new SudokuClassic(2, symbols, new Position(2, 0));
        Grid grid = new Grid.Builder()
                .addSudoku(sudoku1)
                .addSudoku(sudoku2)
                .build();
        assertEquals(new Position(4, 2), grid.getSize());
    }

    @Test
    void testInitColors() {
        Grid grid = new Grid.Builder()
                .addSudoku(new SudokuClassic(2))
                .build();
        grid.initColors();
        assertEquals(4, grid.getRules().size());
    }

    @Test
    void testPrint() {
        Grid grid = new Grid.Builder()
                .addSudoku(new SudokuClassic(2))
                .build();
        grid.initCells();
        grid.insertSymbol("5", new Position(0, 0));
        grid.print();
    }
}