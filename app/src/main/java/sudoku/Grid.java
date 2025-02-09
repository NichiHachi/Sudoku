package sudoku;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sudoku.rule.Rule;
import sudoku.sudoku.Sudoku;

/**
 * The Grid class represents a Sudoku grid, which can contain multiple Sudoku
 * puzzles.
 */
public class Grid {

    private static final Logger logger = LoggerFactory.getLogger(Grid.class);
    private final ArrayList<Rule> rules;
    private Position size;
    private Cell[][] gridCell;
    private final ArrayList<Set<String>> symbols;
    private ArrayList<String> colors;
    private boolean randomBlock = false;

    /**
     * Constructs an empty Grid.
     */
    public Grid() {
        this.rules = new ArrayList<>();
        this.symbols = new ArrayList<>();
    }

    /**
     * Constructs a Grid with the specified size.
     *
     * @param sizeX the width of the grid
     * @param sizeY the height of the grid
     */
    public Grid(int sizeX, int sizeY) {
        this();
        this.size = new Position(sizeX, sizeY);
        this.gridCell = new Cell[sizeY][sizeX];
    }

    /**
     * Constructs a Grid using a Builder.
     *
     * @param builder the Builder to construct the Grid from
     */
    public Grid(Builder builder) {
        this();
        Position minPos = builder.sudokus.getFirst().getMinPosition();
        Position maxPos = builder.sudokus.getFirst().getMaxPosition();
        for (Sudoku sudoku : builder.sudokus) {
            minPos = minPos.min(sudoku.getMinPosition());
            maxPos = maxPos.max(sudoku.getMaxPosition());
            if (sudoku.isRandomBlock()) {
                this.randomBlock = true;
            }
        }
        Position resizeVector = minPos.negative();
        this.size = maxPos.add(resizeVector);

        this.gridCell = new Cell[this.size.getY()][this.size.getX()];

        this.mergeSudokus(builder.sudokus, resizeVector);
        this.initCells();
        this.colors = new ArrayList<>(this.rules.size());
        logger.info("Number of rules: " + this.rules.size());

        this.initColors();
    }

    /**
     * Initializes the colors for the grid cells based on the rules.
     */
    public void initColors() {
        this.colors = new ArrayList<>(this.rules.size());
        for (int i = 0; i < this.rules.size(); i++) {
            Rule rule = this.rules.get(i);
            if (rule instanceof sudoku.rule.BlockRule) {
                Random random = new Random();
                int r = random.nextInt(256);
                int g = random.nextInt(256);
                int b = random.nextInt(256);
                colors.add(i, String.format("\u001B[38;2;%d;%d;%dm", r, g, b));
            } else {
                colors.add(i, "\u001B[38;5;231m");
            }
        }
    }

    /**
     * Checks if the grid has random blocks.
     *
     * @return true if the grid has random blocks, false otherwise
     */
    public boolean isRandomBlock() {
        return this.randomBlock;
    }

    /**
     * Builder class for constructing a Grid.
     */
    public static class Builder {

        private final ArrayList<Sudoku> sudokus = new ArrayList<>();

        /**
         * Adds a Sudoku puzzle to the builder.
         *
         * @param sudoku the Sudoku puzzle to add
         * @return the Builder instance
         */
        public Builder addSudoku(Sudoku sudoku) {
            this.sudokus.add(sudoku);
            return this;
        }

        /**
         * Adds multiple Sudoku puzzles to the builder.
         *
         * @param sudokus the list of Sudoku puzzles to add
         * @return the Builder instance
         */
        public Builder addSudokus(ArrayList<Sudoku> sudokus) {
            this.sudokus.addAll(sudokus);
            return this;
        }

        /**
         * Builds and returns a Grid instance.
         *
         * @return the constructed Grid
         */
        public Grid build() {
            return new Grid(this);
        }
    }

    /**
     * Merges multiple Sudoku puzzles into the grid.
     *
     * @param sudokus      the list of Sudoku puzzles to merge
     * @param resizeVector the vector to resize the grid
     */
    public void mergeSudokus(ArrayList<Sudoku> sudokus, Position resizeVector) {
        for (Sudoku sudoku : sudokus) {
            if (!this.symbols.contains(sudoku.getSymbols())) {
                this.symbols.add(sudoku.getSymbols());
            }
            int index = this.symbols.indexOf(sudoku.getSymbols());
            Position offsetSudoku = sudoku.getOffsetPosition().add(resizeVector);

            for (int i = 0; i < sudoku.getNumberRule(); i++) {
                Rule rule = sudoku.getRule(i);
                rule.setIndexSymbols(index);
                rule.offsetRepositioning(offsetSudoku);
                if (!this.containRule(rule, index)) {
                    this.addRule(rule);
                }
            }
        }
    }

    /**
     * Checks if the grid contains a specific rule.
     *
     * @param rule         the rule to check
     * @param indexSymbols the index of the symbols
     * @return true if the rule is contained in the grid, false otherwise
     */
    private boolean containRule(Rule rule, int indexSymbols) {
        for (Rule existingRule : this.rules) {
            if (existingRule.getRulePositions().equals(rule.getRulePositions()) &&
                    existingRule.getIndexSymbols() == indexSymbols) {
                return true;
            }
        }
        return false;
    }

    /**
     * Initializes the cells of the grid based on the rules.
     */
    public void initCells() {
        for (int i = 0; i < this.rules.size(); i++) {
            Rule rule = this.rules.get(i);
            for (Position position : rule.getRulePositions()) {
                int x = position.getX();
                int y = position.getY();
                if (this.gridCell[y][x] == null) {
                    this.gridCell[y][x] = new Cell();
                }
                this.gridCell[y][x].addRule(i);
            }
        }
    }

    /**
     * Gets the size of the grid.
     *
     * @return the size of the grid
     */
    public Position getSize() {
        return this.size;
    }

    /**
     * Gets the cell at the specified position.
     *
     * @param position the position of the cell
     * @return the cell at the specified position
     */
    public Cell getCell(Position position) {
        return this.gridCell[position.getY()][position.getX()];
    }

    /**
     * Gets the rule at the specified index.
     *
     * @param index the index of the rule
     * @return the rule at the specified index
     */
    public Rule getRule(int index) {
        return this.rules.get(index);
    }

    /**
     * Gets the list of rules in the grid.
     *
     * @return the list of rules
     */
    public ArrayList<Rule> getRules() {
        return this.rules;
    }

    /**
     * Prints the grid to the console.
     */
    public void print() {
        StringBuilder sb = new StringBuilder();
        for (Cell[] cellLine : this.gridCell) {
            for (Cell cell : cellLine) {
                if (cell == null) {
                    sb.append("  ");
                } else {
                    String color = "\u001B[38;5;231m";
                    ArrayList<Integer> idRules = cell.getIdRules();
                    for (int idRule : idRules) {
                        if (rules.get(idRule) instanceof sudoku.rule.BlockRule) {
                            color = colors.get(idRule);
                            break;
                        }
                    }
                    sb.append(color + (cell.getSymbol() != null ? cell.getSymbol() + " " : "- ") + "\u001B[0m");
                }
            }
            sb.append("\n");
        }
        logger.info("\n" + sb.toString());
    }

    /**
     * Inserts a symbol at the specified position in the grid.
     *
     * @param symbol   the symbol to insert
     * @param position the position to insert the symbol at
     */
    public void insertSymbol(String symbol, Position position) {
        if (this.canInsertValue(symbol, position)) {
            this.handleInsertValue(symbol, position);
        }
    }

    /**
     * Handles the insertion of a symbol at the specified position.
     *
     * @param symbol   the symbol to insert
     * @param position the position to insert the symbol at
     */
    private void handleInsertValue(String symbol, Position position) {
        int x = position.getX();
        int y = position.getY();
        this.gridCell[y][x].insertSymbol(symbol);
    }

    /**
     * Checks if a symbol can be inserted at the specified position.
     *
     * @param symbol   the symbol to insert
     * @param position the position to insert the symbol at
     * @return true if the symbol can be inserted, false otherwise
     */
    private boolean canInsertValue(String symbol, Position position) {
        int x = position.getX();
        int y = position.getY();

        if (!this.isInsideGrid(position)) {
            logger.warn("Insert outside of a Sudoku");
            return false;
        }

        for (int indexRule : this.gridCell[y][x].getIdRules()) {
            Rule rule = this.rules.get(indexRule);

            int indexSymbols = rule.getIndexSymbols();
            Set<String> symbols = this.symbols.get(indexSymbols);
            if (!symbols.contains(symbol)) {
                return false;
            }

            for (Position rulePosition : rule.getRulePositions()) {
                if (Objects.equals(this.getSymbol(rulePosition), symbol)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Gets the list of symbols in the grid.
     *
     * @return the list of symbols
     */
    public ArrayList<Set<String>> getSymbols() {
        return this.symbols;
    }

    /**
     * Gets the symbols at the specified index.
     *
     * @param index the index of the symbols
     * @return the symbols at the specified index
     */
    public Set<String> getSymbols(int index) {
        return this.symbols.get(index);
    }

    /**
     * Resets the symbol at the specified position.
     *
     * @param position the position to reset the symbol at
     */
    public void resetSymbol(Position position) {
        if (!this.isInsideGrid(position)) {
            System.err.println("[Grid] Reset outside of a Sudoku");
        }
        this.getCell(position).resetSymbol();
    }

    /**
     * Gets the symbol at the specified position.
     *
     * @param position the position to get the symbol from
     * @return the symbol at the specified position, or null if none exists
     */
    public String getSymbol(Position position) {
        if (!isInsideGrid(position)) {
            return null;
        }
        int x = position.getX();
        int y = position.getY();
        return this.gridCell[y][x].getSymbol();
    }

    /**
     * Checks if the specified position is inside the grid.
     *
     * @param position the position to check
     * @return true if the position is inside the grid, false otherwise
     */
    public boolean isInsideGrid(Position position) {
        return position.getX() >= 0 && position.getY() >= 0 &&
                position.getX() < this.size.getX() && position.getY() < this.size.getY() &&
                this.gridCell[position.getY()][position.getX()] != null;
    }

    /**
     * Removes the cell at the specified position.
     *
     * @param position the position to remove the cell from
     */
    private void removeCell(Position position) {
        int x = position.getX();
        int y = position.getY();
        if (isInsideGrid(position)) {
            this.gridCell[y][x] = null;
        }
    }

    /**
     * Checks if the grid is complete.
     *
     * @return true if the grid is complete, false otherwise
     */
    public boolean isComplete() {
        for (int y = 0; y < this.size.getY(); y++) {
            for (int x = 0; x < this.size.getX(); x++) {
                if (this.gridCell[y][x] != null && this.gridCell[y][x].getSymbol() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Starts the terminal interface for playing the grid.
     */
    public void playTerminal() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (!this.isComplete()) {
                this.print();
                logger.info("Insert value: ");
                String value = scanner.nextLine();
                logger.info("Place " + value + " here: ");
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                scanner.nextLine();
                Position position = new Position(x, y);
                this.insertSymbol(value, position);
            }
            this.print();
        }
    }

    /**
     * Gets the possible plays at the specified position.
     *
     * @param position the position to get the possible plays from
     * @return the set of possible plays
     */
    public Set<String> getPossiblePlays(Position position) {
        int x = position.getX();
        int y = position.getY();
        if (!this.isInsideGrid(position) || this.gridCell[y][x].getSymbol() != null ||
                this.gridCell[y][x].getIdRules().isEmpty()) {
            return new HashSet<>();
        }

        Set<String> intersection = new HashSet<>();
        boolean firstRule = true;

        for (int indexRule : this.gridCell[y][x].getIdRules()) {
            Rule rule = this.rules.get(indexRule);
            if (firstRule) {
                intersection.addAll(this.symbols.get(rule.getIndexSymbols()));
                firstRule = false;
            }
            intersection.removeAll(this.symbolUsed(rule));
        }
        return intersection;
    }

    /**
     * Gets the symbols used in the specified rule.
     *
     * @param rule the rule to get the symbols from
     * @return the set of symbols used in the rule
     */
    public Set<String> symbolUsed(Rule rule) {
        Set<String> symbols = new HashSet<>();
        for (Position position : rule.getRulePositions()) {
            if (this.getSymbol(position) != null) {
                symbols.add(this.getSymbol(position));
            }
        }
        return symbols;
    }

    /**
     * Counts the possible plays at the specified position.
     *
     * @param position the position to count the possible plays at
     * @return the number of possible plays
     */
    public int countPossiblePlays(Position position) {
        return this.getPossiblePlays(position).size();
    }

    /**
     * Adds a rule to the grid.
     *
     * @param rule the rule to add
     */
    public void addRule(Rule rule) {
        this.rules.add(rule);
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        int result = 1;
        for (int y = 0; y < this.size.getY(); y++) {
            for (int x = 0; x < this.size.getX(); x++) {
                Cell cell = this.gridCell[y][x];
                result = 31 * result + (cell != null && cell.getSymbol() != null ? cell.getSymbol().hashCode() : 0);
            }
        }
        return result;
    }

    /**
     * Sets the cell at the specified position.
     *
     * @param position the position to set the cell at
     * @param cell     the cell to set
     */
    public void setCell(Position position, Cell cell) {
        this.gridCell[position.getY()][position.getX()] = cell;
    }

    /**
     * Sets the rules for the grid.
     *
     * @param rules the list of rules to set
     */
    public void setRules(ArrayList<Rule> rules) {
        this.rules.addAll(rules);
    }

    /**
     * Gets the list of symbols in the grid.
     *
     * @return the list of symbols
     */
    public ArrayList<Set<String>> getSymboles() {
        return this.symbols;
    }

    /**
     * Sets the symbols for the grid.
     *
     * @param symboles the list of symbols to set
     */
    public void setSymboles(ArrayList<Set<String>> symboles) {
        this.symbols.addAll(symboles);
    }

    /**
     * Gets the number of non-null cells in the grid.
     *
     * @return the number of non-null cells
     */
    public int getNbOfCellNotNull() {
        int count = 0;
        for (int y = 0; y < this.size.getY(); y++) {
            for (int x = 0; x < this.size.getX(); x++) {
                if (this.gridCell[y][x] != null) {
                    count++;
                }
            }
        }
        return count;
    }
}