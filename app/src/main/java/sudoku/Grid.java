package sudoku;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Grid {
    ArrayList<Rule> rules;
    private Position size;
    private Cell[][] gridCell;

    public Grid(ArrayList<Sudoku> sudokus) {
        // Init the size of the Grid
        // Get the min and max Position between each Sudoku for index and size purpose
        Position minPos = sudokus.getFirst().getMinPosition();
        Position maxPos = sudokus.getFirst().getMaxPosition();
        for (Sudoku sudoku : sudokus) {
            minPos = minPos.min(sudoku.getMinPosition());
            maxPos = maxPos.max(sudoku.getMaxPosition());
        }
        Position resizeVector = minPos.negative();
        this.size = maxPos.add(resizeVector);

        this.gridCell = new Cell[this.size.getY()][this.size.getX()];

        // Init the rules
        ArrayList<Set<Position>> rulesPositions = new ArrayList<>();
        this.rules = new ArrayList<>();
        for (Sudoku sudoku : sudokus) {
            for (int i = 0; i < sudoku.getNumberRule(); i++) {
                Set<Position> ruleAbsolutePositions = adjustPositions(sudoku.getRulePositions(i), resizeVector);
                Rule rule = sudoku.getRule(i);
                int index = rulesPositions.indexOf(ruleAbsolutePositions);
                if (index == -1) {
                    rulesPositions.add(ruleAbsolutePositions);
                    this.rules.add(rule);
                } else {
                    this.rules.get(index).mergingRule(rule);
                }
            }
        }

        // Init Cells in the gridCell
        for (int i = 0; i < rulesPositions.size(); i++) {
            for (Position pos : rulesPositions.get(i)) {
                int x = pos.getX();
                int y = pos.getY();
                if (this.gridCell[y][x] == null) {
                    this.gridCell[y][x] = new Cell(i);
                } else {
                    this.gridCell[y][x].addRule(i);
                }
            }
        }
    }

    public Cell getCell(Position position) {
        return this.gridCell[position.getY()][position.getX()];
    }

    public void reset(ArrayList<Rule> rules) {
        for (int y = 0; y < this.size.getY(); y++) {
            for (int x = 0; x < this.size.getX(); x++) {
                if (this.gridCell[y][x] != null) {
                    this.gridCell[y][x].resetValue();
                }
            }
        }
        this.rules = rules;
    }

    public void print() {
        for (Cell[] cellLine : this.gridCell) {
            for (Cell cell : cellLine) {
                if (cell == null) {
                    System.out.print("  ");
                } else {
                    String color = getColor(cell.getNumberOfRules());
                    System.out.print(color + (cell.getValue() != null ? cell.getValue() + " " : "- ") + "\u001B[0m");
                }
            }
            System.out.println();
        }
    }

    public void insertValue(String value, Position position) {
        if (this.canInsertValue(value, position)) {
            this.handleInsertValue(value, position);
        }
    }

    private void handleInsertValue(String value, Position position) {
        int x = position.getX();
        int y = position.getY();
        this.gridCell[y][x].insertValue(value);
        for (int idRule : this.gridCell[y][x].getIdRules()) {
            this.rules.get(idRule).handleInsertValue(value);
        }
    }

    public boolean canInsertValue(String value, Position position) {
        int x = position.getX();
        int y = position.getY();

        if (!this.isInsideGrid(position) || this.gridCell[y][x] == null) {
            System.err.println("[Grid] Insert outside of a Sudoku");
            return false;
        }

        for (int ruleId : this.gridCell[y][x].getIdRules()) {
            if (!this.rules.get(ruleId).isValid(value)) {
                System.out.println("Can't place " + value + " here");
                return false;
            }
        }

        return true;
    }

    private boolean isInsideGrid(Position position) {
        return position.getX() >= 0 && position.getY() >= 0
                && position.getX() < this.size.getX() && position.getY() < this.size.getY()
                && this.gridCell[position.getY()][position.getX()] != null;
    }

    private void removeCell(Position position) {
        int x = position.getX();
        int y = position.getY();
        if (isInsideGrid(position)) {
            this.gridCell[y][x] = null;
        }
    }

    public boolean isComplete() {
        for (int y = 0; y < this.size.getY(); y++) {
            for (int x = 0; x < this.size.getX(); x++) {
                if (this.gridCell[y][x] != null && this.gridCell[y][x].getValue() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public void playTerminal() {
        Scanner scanner = new Scanner(System.in);
        while (!this.isComplete()) {
            this.print();
            System.out.print("Insert value: ");
            String value = scanner.nextLine();
            System.out.print("Place " + value + " here: ");
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            scanner.nextLine();
            Position position = new Position(x, y);
            this.insertValue(value, position);
        }
        this.print();
    }

    public Set<String> getPossiblePlays(Position position) {
        int x = position.getX();
        int y = position.getY();
        if (!this.isInsideGrid(position) || this.gridCell[y][x].getValue() != null
                || this.gridCell[y][x].getIdRules().isEmpty()) {
            return new HashSet<>();
        }

        Set<String> intersection = this.rules.get(this.gridCell[y][x].getIdRules().getFirst()).getPossibleMove();

        for (int indexRule : this.gridCell[y][x].getIdRules()) {
            intersection.retainAll(this.rules.get(indexRule).getPossibleMove());
        }

        return intersection;
    }

    public int countPossiblePlays(Position position) {
        return this.getPossiblePlays(position).size();
    }

    public void addRule(int idRule, String value, Set<String> rule) {
        this.rules.get(idRule).add(value, rule);
    }

    public ArrayList<Set<String>> getRules(Position position, String value) {
        ArrayList<Set<String>> rules = new ArrayList<>();
        ArrayList<Integer> idRules = this.gridCell[position.getY()][position.getX()].getIdRules();
        for (int idRule : idRules) {
            rules.add(this.rules.get(idRule).get(value));
        }
        return rules;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (int y = 0; y < this.size.getY(); y++) {
            for (int x = 0; x < this.size.getX(); x++) {
                Cell cell = this.gridCell[y][x];
                result = 31 * result + (cell != null && cell.getValue() != null ? cell.getValue().hashCode() : 0);
            }
        }
        return result;
    }

    String getColor(int ruleCount) {
        String[] colors = {
                "\u001B[38;5;231m", // White
                "\u001B[38;5;226m", // Yellow
                "\u001B[38;5;220m", // Light Orange
                "\u001B[38;5;214m", // Orange
                "\u001B[38;5;208m", // Dark Orange
                "\u001B[38;5;202m", // Red-Orange
                "\u001B[38;5;196m", // Red
                "\u001B[38;5;201m", // Magenta
                "\u001B[38;5;93m", // Light Purple
                "\u001B[38;5;57m", // Purple
                "\u001B[38;5;21m", // Blue
                "\u001B[38;5;51m", // Cyan
                "\u001B[38;5;46m", // Green
                "\u001B[38;5;118m" // Light Green
        };
        return colors[ruleCount % colors.length];
    }

    Set<Position> adjustPositions(Set<Position> positions, Position adjustPosition) {
        Set<Position> absolutePositions = new HashSet<>();
        for (Position position : positions) {
            absolutePositions.add(position.add(adjustPosition));
        }
        return absolutePositions;
    }

    public Position getSize() {
        return this.size;
    }

    public Cell getCell(int x, int y) {
        return this.gridCell[y][x];
    }

}