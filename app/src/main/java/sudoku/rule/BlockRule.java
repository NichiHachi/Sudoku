package sudoku.rule;

import java.util.Set;

import sudoku.Position;

/**
 * The BlockRule class represents a rule that applies to a block in a Sudoku
 * puzzle.
 */
public class BlockRule extends Rule {

    /**
     * Constructs an empty BlockRule.
     */
    public BlockRule() {
        super();
    }

    /**
     * Constructs a BlockRule with a set of positions.
     *
     * @param positions the set of positions to add to the rule
     */
    public BlockRule(Set<Position> positions) {
        super(positions);
    }

    /**
     * Constructs a BlockRule from a starting position to an ending position.
     *
     * @param positionStart the starting position of the block
     * @param positionEnd   the ending position of the block
     */
    public BlockRule(Position positionStart, Position positionEnd) {
        this();

        if (positionStart.getX() > positionEnd.getX()) {
            int temp = positionStart.getX();
            positionStart.setX(positionEnd.getX());
            positionEnd.setX(temp);
        }

        if (positionStart.getY() > positionEnd.getY()) {
            int temp = positionStart.getY();
            positionStart.setY(positionEnd.getY());
            positionEnd.setY(temp);
        }

        for (int y = positionStart.getY(); y <= positionEnd.getY(); y++) {
            for (int x = positionStart.getX(); x <= positionEnd.getX(); x++) {
                this.add(new Position(x, y));
            }
        }
    }
}