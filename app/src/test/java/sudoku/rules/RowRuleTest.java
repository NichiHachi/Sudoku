package sudoku.rules;

import org.junit.jupiter.api.Test;
import sudoku.Position;
import sudoku.rule.RowRule;

import static org.junit.jupiter.api.Assertions.*;

class RowRuleTest {

    @Test
    void testRowRuleWithLength() {
        Position start = new Position(0, 0);
        RowRule rowRule = new RowRule(start, 3);
        assertTrue(rowRule.getRulePositions().contains(new Position(0, 0)));
        assertTrue(rowRule.getRulePositions().contains(new Position(1, 0)));
        assertTrue(rowRule.getRulePositions().contains(new Position(2, 0)));
    }

    @Test
    void testRowRuleWithPositionRange() {
        Position start = new Position(0, 0);
        Position end = new Position(2, 0);
        RowRule rowRule = new RowRule(start, end);
        assertTrue(rowRule.getRulePositions().contains(new Position(0, 0)));
        assertTrue(rowRule.getRulePositions().contains(new Position(1, 0)));
        assertTrue(rowRule.getRulePositions().contains(new Position(2, 0)));
    }
}