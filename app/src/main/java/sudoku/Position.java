package sudoku;

import java.util.Objects;

/**
 * The Position class represents a coordinate with x and y values.
 */
public class Position {
    private int x;
    private int y;

    /**
     * Constructs a Position with the specified x and y values.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a Position with the same x and y values.
     *
     * @param z the value for both x and y coordinates
     */
    public Position(int z) {
        this.x = z;
        this.y = z;
    }

    /**
     * Gets the x-coordinate.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Adds the coordinates of another Position to this Position and returns a new
     * Position.
     *
     * @param pos the Position to add
     * @return a new Position with the added coordinates
     */
    public Position add(Position pos) {
        return new Position(this.x + pos.x, this.y + pos.y);
    }

    /**
     * Adds a value to the x-coordinate and returns a new Position.
     *
     * @param x the value to add to the x-coordinate
     * @return a new Position with the added x-coordinate
     */
    public Position addX(int x) {
        return new Position(this.x + x, this.y);
    }

    /**
     * Adds a value to the y-coordinate and returns a new Position.
     *
     * @param y the value to add to the y-coordinate
     * @return a new Position with the added y-coordinate
     */
    public Position addY(int y) {
        return new Position(this.x, this.y + y);
    }

    /**
     * Adds a value to both the x and y coordinates and returns a new Position.
     *
     * @param z the value to add to both coordinates
     * @return a new Position with the added coordinates
     */
    public Position add(int z) {
        return new Position(this.x + z, this.y + z);
    }

    /**
     * Adds the coordinates of another Position to this Position.
     *
     * @param pos the Position to add
     */
    public void addi(Position pos) {
        this.x = this.x + pos.x;
        this.y = this.y + pos.y;
    }

    /**
     * Adds a value to the x-coordinate of this Position.
     *
     * @param x the value to add to the x-coordinate
     */
    public void addXi(int x) {
        this.x = this.x + x;
    }

    /**
     * Adds a value to the y-coordinate of this Position.
     *
     * @param y the value to add to the y-coordinate
     */
    public void addYi(int y) {
        this.y = this.y + y;
    }

    /**
     * Sets the x-coordinate of this Position.
     *
     * @param x the new x-coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate of this Position.
     *
     * @param y the new y-coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Returns a new Position with the maximum x and y values of this Position and
     * another Position.
     *
     * @param position the other Position
     * @return a new Position with the maximum coordinates
     */
    public Position max(Position position) {
        return new Position(Math.max(this.x, position.x), Math.max(this.y, position.y));
    }

    /**
     * Returns a new Position with the minimum x and y values of this Position and
     * another Position.
     *
     * @param position the other Position
     * @return a new Position with the minimum coordinates
     */
    public Position min(Position position) {
        return new Position(Math.min(this.x, position.x), Math.min(this.y, position.y));
    }

    /**
     * Returns a new Position with the negated x and y values of this Position.
     *
     * @return a new Position with negated coordinates
     */
    public Position negative() {
        return new Position(-this.x, -this.y);
    }

    /**
     * Returns a string representation of this Position.
     *
     * @return a string representation of this Position
     */
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj the reference object with which to compare
     * @return true if this object is the same as the obj argument; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}