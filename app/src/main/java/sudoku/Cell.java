package sudoku;

public class Cell {
    private Position position;
    private Element element;
    private int[] idConstraint;

    public Cell(Position position, Element element, int[] idConstraint) {
        this.position = position;
        this.element = element;
        this.idConstraint = idConstraint;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public int[] getIdConstraints() {
        return idConstraint;
    }

    public void setIdConstraints(int[] idConstraint) {
        this.idConstraint = idConstraint;
    }

}
