package sudoku.sudokus;

import org.junit.jupiter.api.Test;
import sudoku.Position;
import sudoku.rule.Rule;
import sudoku.sudoku.Sudoku;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SudokuTest {

    @Test
    void testSudokuInitialization() {
        Set<String> symbols = new HashSet<>();
        for (int i = 1; i <= 9; i++) {
            symbols.add(Integer.toString(i));
        }
        Sudoku sudoku = new Sudoku(9, symbols);
        assertEquals(9, sudoku.getSize());
        assertEquals(symbols, sudoku.getSymbols());
    }

    @Test
    void testAddRule() {
        Set<String> symbols = new HashSet<>();
        for (int i = 1; i <= 9; i++) {
            symbols.add(Integer.toString(i));
        }
        Sudoku sudoku = new Sudoku(9, symbols);
        Rule rule = new Rule();
        rule.add(new Position(0, 0));
        sudoku.add(rule);
        assertEquals(1, sudoku.getNumberRule());
    }

    @Test
    void testRemoveRule() {
        Set<String> symbols = new HashSet<>();
        for (int i = 1; i <= 9; i++) {
            symbols.add(Integer.toString(i));
        }
        Sudoku sudoku = new Sudoku(9, symbols);
        Rule rule = new Rule();
        rule.add(new Position(0, 0));
        sudoku.add(rule);
        sudoku.remove(rule);
        assertEquals(0, sudoku.getNumberRule());
    }

    @Test
    void testGetRule() {
        Set<String> symbols = new HashSet<>();
        for (int i = 1; i <= 9; i++) {
            symbols.add(Integer.toString(i));
        }
        Sudoku sudoku = new Sudoku(9, symbols);
        Rule rule = new Rule();
        rule.add(new Position(0, 0));
        sudoku.add(rule);
        assertEquals(rule, sudoku.getRule(0));
    }

    @Test
    void testAddColumnRules() {
        Set<String> symbols = new HashSet<>();
        for (int i = 1; i <= 9; i++) {
            symbols.add(Integer.toString(i));
        }
        Sudoku sudoku = new Sudoku(9, symbols);
        sudoku.addColumnRules();
        assertEquals(9, sudoku.getNumberRule());
    }

    @Test
    void testAddRowRules() {
        Set<String> symbols = new HashSet<>();
        for (int i = 1; i <= 9; i++) {
            symbols.add(Integer.toString(i));
        }
        Sudoku sudoku = new Sudoku(9, symbols);
        sudoku.addRowRules();
        assertEquals(9, sudoku.getNumberRule());
    }
}