package sudoku.rule;

import java.util.Set;

import sudoku.Position;

public class RowRule extends Rule {
    public RowRule() {
        super();
    }

    public RowRule(Set<Position> positions) {
        super(positions);
    }

    public RowRule(Position positionStart, int length) {
        this();
        for (int i = 0; i < length; i++) {
            this.add(positionStart.addX(i));
        }
    }

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