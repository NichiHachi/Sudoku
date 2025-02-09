package sudoku.sudokus;

import org.junit.jupiter.api.Test;
import sudoku.Position;
import sudoku.sudoku.SudokuClassic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SudokuClassicTest {

    @Test
    void testSudokuClassicInitialization() {
        Set<String> symbols = new HashSet<>();
        for (int i = 1; i <= 9; i++) {
            symbols.add(Integer.toString(i));
        }
        SudokuClassic sudokuClassic = new SudokuClassic(9, symbols);
        assertEquals(9, sudokuClassic.getSize());
        assertEquals(symbols, sudokuClassic.getSymbols());
    }

    @Test
    void testSudokuClassicWithOffset() {
        Set<String> symbols = new HashSet<>();
        for (int i = 1; i <= 9; i++) {
            symbols.add(Integer.toString(i));
        }
        Position offset = new Position(1, 1);
        SudokuClassic sudokuClassic = new SudokuClassic(9, symbols, offset);
        assertEquals(offset, sudokuClassic.getOffsetPosition());
    }

    @Test
    void testPrimeFactors() {
        assertEquals(List.of(3, 3), SudokuClassic.primeFactors(9));
        assertEquals(List.of(2, 2), SudokuClassic.primeFactors(4));
        assertEquals(List.of(2, 3), SudokuClassic.primeFactors(6));
    }

    @Test
    void testAddBlockRules() {
        Set<String> symbols = new HashSet<>();
        for (int i = 1; i <= 9; i++) {
            symbols.add(Integer.toString(i));
        }
        SudokuClassic sudokuClassic = new SudokuClassic(9, symbols);
        assertEquals(27, sudokuClassic.getNumberRule()); // 9 row rules + 9 column rules + 9 block rules
    }
}