package sudoku.rules;

import org.junit.jupiter.api.Test;
import sudoku.Position;
import sudoku.rule.Rule;

import static org.junit.jupiter.api.Assertions.*;

class RuleTest {

    @Test
    void testAddPosition() {
        Rule rule = new Rule();
        Position position = new Position(1, 1);
        rule.add(position);
        assertTrue(rule.getRulePositions().contains(position));
    }

    @Test
    void testOffsetRepositioning() {
        Rule rule = new Rule();
        Position position = new Position(1, 1);
        rule.add(position);
        rule.offsetRepositioning(new Position(1, 1));
        assertTrue(rule.getRulePositions().contains(new Position(2, 2)));
    }

    @Test
    void testSetAndGetIndexSymbols() {
        Rule rule = new Rule();
        rule.setIndexSymbols(5);
        assertEquals(5, rule.getIndexSymbols());
    }

    @Test
    void testToString() {
        Rule rule = new Rule();
        rule.add(new Position(1, 1));
        rule.add(new Position(2, 2));
        String expected = "Rule Positions: [(1,1), (2,2)]";
        assertEquals(expected, rule.toString());
    }
}