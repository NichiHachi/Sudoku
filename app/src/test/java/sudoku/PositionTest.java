package sudoku;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void testPositionInitialization() {
        Position pos = new Position(3, 4);
        assertEquals(3, pos.getX());
        assertEquals(4, pos.getY());
    }

    @Test
    void testSingleValueInitialization() {
        Position pos = new Position(5);
        assertEquals(5, pos.getX());
        assertEquals(5, pos.getY());
    }

    @Test
    void testAddPosition() {
        Position pos1 = new Position(1, 2);
        Position pos2 = new Position(3, 4);
        Position result = pos1.add(pos2);
        assertEquals(new Position(4, 6), result);
    }

    @Test
    void testAddX() {
        Position pos = new Position(1, 2);
        Position result = pos.addX(3);
        assertEquals(new Position(4, 2), result);
    }

    @Test
    void testAddY() {
        Position pos = new Position(1, 2);
        Position result = pos.addY(3);
        assertEquals(new Position(1, 5), result);
    }

    @Test
    void testAddSingleValue() {
        Position pos = new Position(1, 2);
        Position result = pos.add(3);
        assertEquals(new Position(4, 5), result);
    }

    @Test
    void testAddi() {
        Position pos1 = new Position(1, 2);
        Position pos2 = new Position(3, 4);
        pos1.addi(pos2);
        assertEquals(new Position(4, 6), pos1);
    }

    @Test
    void testAddXi() {
        Position pos = new Position(1, 2);
        pos.addXi(3);
        assertEquals(new Position(4, 2), pos);
    }

    @Test
    void testAddYi() {
        Position pos = new Position(1, 2);
        pos.addYi(3);
        assertEquals(new Position(1, 5), pos);
    }

    @Test
    void testSetX() {
        Position pos = new Position(1, 2);
        pos.setX(5);
        assertEquals(5, pos.getX());
    }

    @Test
    void testSetY() {
        Position pos = new Position(1, 2);
        pos.setY(5);
        assertEquals(5, pos.getY());
    }

    @Test
    void testMax() {
        Position pos1 = new Position(1, 5);
        Position pos2 = new Position(3, 2);
        Position result = pos1.max(pos2);
        assertEquals(new Position(3, 5), result);
    }

    @Test
    void testMin() {
        Position pos1 = new Position(1, 5);
        Position pos2 = new Position(3, 2);
        Position result = pos1.min(pos2);
        assertEquals(new Position(1, 2), result);
    }

    @Test
    void testNegative() {
        Position pos = new Position(1, 2);
        Position result = pos.negative();
        assertEquals(new Position(-1, -2), result);
    }

    @Test
    void testToString() {
        Position pos = new Position(1, 2);
        assertEquals("(1,2)", pos.toString());
    }

    @Test
    void testEquals() {
        Position pos1 = new Position(1, 2);
        Position pos2 = new Position(1, 2);
        assertEquals(pos1, pos2);
    }

    @Test
    void testNotEquals() {
        Position pos1 = new Position(1, 2);
        Position pos2 = new Position(2, 1);
        assertNotEquals(pos1, pos2);
    }

    @Test
    void testHashCode() {
        Position pos1 = new Position(1, 2);
        Position pos2 = new Position(1, 2);
        assertEquals(pos1.hashCode(), pos2.hashCode());
    }
}