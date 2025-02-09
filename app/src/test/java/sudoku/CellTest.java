package sudoku;

import org.junit.jupiter.api.Test;
import sudoku.rule.BlockRule;
import sudoku.rule.Rule;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    @Test
    void testCellInitialization() {
        Cell cell = new Cell();
        assertNull(cell.getSymbol());
        assertEquals(0, cell.getNumberOfRules());
    }

    @Test
    void testCellInitializationWithIdRule() {
        Cell cell = new Cell(0);
        assertEquals(1, cell.getNumberOfRules());
        assertEquals(0, cell.getIdRule(0));
    }

    @Test
    void testAddRule() {
        Cell cell = new Cell();
        cell.addRule(0);
        assertEquals(1, cell.getNumberOfRules());
        assertEquals(0, cell.getIdRule(0));
    }

    @Test
    void testDeleteRule() {
        Cell cell = new Cell();
        cell.addRule(1);
        cell.deleteRule(1);
        assertEquals(0, cell.getNumberOfRules());
    }

    @Test
    void testInsertSymbol() {
        Cell cell = new Cell();
        cell.insertSymbol("5");
        assertEquals("5", cell.getSymbol());
    }

    @Test
    void testInsertSymbolWhenAlreadyPresent() {
        Cell cell = new Cell();
        cell.insertSymbol("5");
        cell.insertSymbol("3");
        assertEquals("5", cell.getSymbol());
    }

    @Test
    void testResetSymbol() {
        Cell cell = new Cell();
        cell.insertSymbol("5");
        cell.resetSymbol();
        assertNull(cell.getSymbol());
    }

    @Test
    void testGetIdRuleOutOfRange() {
        Cell cell = new Cell();
        assertEquals(-1, cell.getIdRule(0));
    }

    @Test
    void testGetIdRules() {
        Cell cell = new Cell();
        cell.addRule(0);
        cell.addRule(1);
        ArrayList<Integer> idRules = cell.getIdRules();
        assertEquals(2, idRules.size());
        assertTrue(idRules.contains(0));
        assertTrue(idRules.contains(1));
    }

    @Test
    void testGetNumberOfPrintableRules() {
        Cell cell = new Cell();
        cell.addRule(0);
        cell.addRule(1);
        ArrayList<Rule> rules = new ArrayList<>();
        rules.add(new BlockRule(new Position(0, 0), new Position(1, 1)));
        rules.add(new Rule());
        assertEquals(1, cell.getNumberOfPrintableRules(rules));
    }
}