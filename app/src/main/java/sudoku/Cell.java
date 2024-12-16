package sudoku;

public class Cell {
    private Position position;
    private Element element;
    private Constraint[] constraints;

    public Cell(Position position, Element element, Constraint[] constraints) {
        this.position = position;
        this.element = element;
        this.constraints = constraints;
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

    public Constraint[] getConstraints() {
        return constraints;
    }

    public void setConstraints(Constraint[] constraints) {
        this.constraints = constraints;
    }

}
