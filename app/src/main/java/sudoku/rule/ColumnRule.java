package sudoku.rule;

import java.util.Set;

import sudoku.Position;

public class ColumnRule extends Rule {
    public ColumnRule() {
        super();
    }

    public ColumnRule(Set<Position> positions) {
        super(positions);
    }

    public ColumnRule(Position positionStart, int height) {
        this();
        for (int i = 0; i < height; i++) {
            this.add(positionStart.addY(i));
        }
    }

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
