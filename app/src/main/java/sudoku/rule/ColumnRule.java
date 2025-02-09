package sudoku.rule;

import java.util.Set;

import sudoku.Position;

/**
 * The ColumnRule class represents a rule that applies to a column in a Sudoku
 * puzzle.
 */
public class ColumnRule extends Rule {

    /**
     * Constructs an empty ColumnRule.
     */
    public ColumnRule() {
        super();
    }

    /**
     * Constructs a ColumnRule with a set of positions.
     *
     * @param positions the set of positions to add to the rule
     */
    public ColumnRule(Set<Position> positions) {
        super(positions);
    }

    /**
     * Constructs a ColumnRule starting from a position with a specified height.
     *
     * @param positionStart the starting position of the column
     * @param height        the height of the column
     */
    public ColumnRule(Position positionStart, int height) {
        this();
        for (int i = 0; i < height; i++) {
            this.add(positionStart.addY(i));
        }
    }

    /**
     * Constructs a ColumnRule from a starting position to an ending position.
     *
     * @param positionStart the starting position of the column
     * @param positionEnd   the ending position of the column
     */
    public ColumnRule(Position positionStart, Position positionEnd) {
        this();

        if (positionStart.getX() != positionEnd.getX()) {
            System.err.println("[ColumnRule] The start and end position must have the same X value");
            return;
        }

        if (positionStart.getY() > positionEnd.getY()) {
            Position temp = positionStart;
            positionStart = positionEnd;
            positionEnd = temp;
        }

        for (int i = positionStart.getY(); i <= positionEnd.getY(); i++) {
            this.add(new Position(positionStart.getX(), i));
        }
    }
}