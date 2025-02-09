package sudoku.rules;

import org.junit.jupiter.api.Test;
import sudoku.Position;
import sudoku.rule.ColumnRule;

import static org.junit.jupiter.api.Assertions.*;

class ColumnRuleTest {

    @Test
    void testColumnRuleWithHeight() {
        Position start = new Position(0, 0);
        ColumnRule columnRule = new ColumnRule(start, 3);
        assertTrue(columnRule.getRulePositions().contains(new Position(0, 0)));
        assertTrue(columnRule.getRulePositions().contains(new Position(0, 1)));
        assertTrue(columnRule.getRulePositions().contains(new Position(0, 2)));
    }

    @Test
    void testColumnRuleWithPositionRange() {
        Position start = new Position(0, 0);
        Position end = new Position(0, 2);
        ColumnRule columnRule = new ColumnRule(start, end);
        assertTrue(columnRule.getRulePositions().contains(new Position(0, 0)));
        assertTrue(columnRule.getRulePositions().contains(new Position(0, 1)));
        assertTrue(columnRule.getRulePositions().contains(new Position(0, 2)));
    }
}