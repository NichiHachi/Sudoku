package sudoku.rule;

import java.util.Set;

import sudoku.Position;

public class BlockRule extends Rule {
    public BlockRule() {
        super();
    }

    public BlockRule(Set<Position> positions) {
        super(positions);
    }

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
