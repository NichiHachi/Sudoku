package sudoku.rules;

import org.junit.jupiter.api.Test;
import sudoku.Position;
import sudoku.rule.BlockRule;

import static org.junit.jupiter.api.Assertions.*;

class BlockRuleTest {

    @Test
    void testBlockRuleWithPositionRange() {
        Position start = new Position(0, 0);
        Position end = new Position(1, 1);
        BlockRule blockRule = new BlockRule(start, end);
        assertTrue(blockRule.getRulePositions().contains(new Position(0, 0)));
        assertTrue(blockRule.getRulePositions().contains(new Position(0, 1)));
        assertTrue(blockRule.getRulePositions().contains(new Position(1, 0)));
        assertTrue(blockRule.getRulePositions().contains(new Position(1, 1)));
    }
}