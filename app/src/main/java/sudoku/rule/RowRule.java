package sudoku.rule;

import java.util.Set;

import sudoku.Position;

/**
 * The RowRule class represents a rule that applies to a row in a Sudoku puzzle.
 */
public class RowRule extends Rule {

    /**
     * Constructs an empty RowRule.
     */
    public RowRule() {
        super();
    }

    /**
     * Constructs a RowRule with a set of positions.
     *
     * @param positions the set of positions to add to the rule
     */
    public RowRule(Set<Position> positions) {
        super(positions);
    }

    /**
     * Constructs a RowRule starting from a position with a specified length.
     *
     * @param positionStart the starting position of the row
     * @param length        the length of the row
     */
    public RowRule(Position positionStart, int length) {
        this();
        for (int i = 0; i < length; i++) {
            this.add(positionStart.addX(i));
        }
    }

    /**
     * Constructs a RowRule from a starting position to an ending position.
     *
     * @param positionStart the starting position of the row
     * @param positionEnd   the ending position of the row
     */
    public RowRule(Position positionStart, Position positionEnd) {
        this();

        if (positionStart.getY() != positionEnd.getY()) {
            System.err.println("[RowRule] The start and end position must have the same Y value");
            return;
        }

        if (positionStart.getX() > positionEnd.getX()) {
            Position temp = positionStart;
            positionStart = positionEnd;
            positionEnd = temp;
        }

        for (int i = positionStart.getX(); i <= positionEnd.getX(); i++) {
            this.add(new Position(i, positionStart.getY()));
        }
    }
}