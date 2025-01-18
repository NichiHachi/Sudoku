package sudoku;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Grid {
    ArrayList<Rule> rules;
    Position size;
    Cell[][] gridCell;

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
            for (int i=0; i<sudoku.getNumberRule(); i++) {
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
        for(int i=0; i<rulesPositions.size(); i++){
            for(Position pos : rulesPositions.get(i)){
                int x = pos.getX();
                int y = pos.getY();
                if(this.gridCell[y][x] == null){
                    this.gridCell[y][x] = new Cell(i);
                } else {
                    this.gridCell[y][x].addRule(i);
                }
            }
        }
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
                "\u001B[38;5;93m",  // Light Purple
                "\u001B[38;5;57m",  // Purple
                "\u001B[38;5;21m",  // Blue
                "\u001B[38;5;51m",  // Cyan
                "\u001B[38;5;46m",  // Green
                "\u001B[38;5;118m"  // Light Green
        };
        return colors[ruleCount % colors.length];
    }

    Set<Position> adjustPositions(Set<Position> positions, Position adjustPosition){
        Set<Position> absolutePositions = new HashSet<>();
        for(Position position : positions){
            absolutePositions.add(position.add(adjustPosition));
        }
        return absolutePositions;
    }
}