package sudoku;

import java.util.Objects;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(int z){
        this.x = z;
        this.y = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Position add(Position pos){
        return new Position(this.x + pos.x, this.y + pos.y);
    }

    public void addi(Position pos){
        this.x = this.x + pos.x;
        this.y = this.y + pos.y;
    }

    public Position max(Position position){
        return new Position(Math.max(this.x, position.x), Math.max(this.y, position.y));
    }

    public Position min(Position position){
        return new Position(Math.min(this.x, position.x), Math.min(this.y, position.y));
    }

    public Position negative(){
        return new Position(-this.x, -this.y);
    }

    public String toString(){
        return "(" + this.x + "," + this.y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
