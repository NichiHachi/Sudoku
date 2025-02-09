package sudoku;

import java.util.ArrayList;

import sudoku.rule.BlockRule;
import sudoku.rule.Rule;

/**
 * The Cell class represents a cell in a Sudoku grid.
 * It contains a symbol and a list of rule IDs that apply to the cell.
 */
public class Cell {
    private String symbol;
    private final ArrayList<Integer> idRules;

    /**
     * Constructs a Cell with the specified list of rule IDs.
     *
     * @param idRules the list of rule IDs
     */
    public Cell(ArrayList<Integer> idRules) {
        this.idRules = idRules;
    }

    /**
     * Constructs a Cell with a single rule ID.
     *
     * @param idRule the rule ID
     */
    public Cell(Integer idRule) {
        this();
        this.idRules.add(idRule);
    }

    /**
     * Constructs an empty Cell with no symbol and no rules.
     */
    public Cell() {
        this.idRules = new ArrayList<>();
    }

    /**
     * Adds a rule ID to the cell.
     *
     * @param idRule the rule ID to add
     */
    public void addRule(Integer idRule) {
        this.idRules.add(idRule);
    }

    /**
     * Deletes a rule ID from the cell.
     *
     * @param idRule the rule ID to delete
     */
    public void deleteRule(Integer idRule) {
        this.idRules.remove(idRule);
    }

    /**
     * Inserts a symbol into the cell.
     *
     * @param symbol the symbol to insert
     */
    public void insertSymbol(String symbol) {
        if (this.symbol != null) {
            System.err.println("[Cell] A value is already inside of the grid (" + this.symbol + ")");
            return;
        }
        this.symbol = symbol;
    }

    /**
     * Gets the symbol in the cell.
     *
     * @return the symbol in the cell, or null if none exists
     */
    public String getSymbol() {
        return this.symbol;
    }

    /**
     * Gets the rule ID at the specified index.
     *
     * @param index the index of the rule ID
     * @return the rule ID at the specified index, or -1 if the index is out of
     *         range
     */
    public int getIdRule(int index) {
        if (index < 0 || index >= this.idRules.size()) {
            System.err
                    .println("[Cell] getIdRule index out of range. Index: " + index + ", Size: " + this.idRules.size());
            return -1;
        }
        return this.idRules.get(index);
    }

    /**
     * Resets the symbol in the cell to null.
     */
    public void resetSymbol() {
        this.symbol = null;
    }

    /**
     * Gets the list of rule IDs that apply to the cell.
     *
     * @return the list of rule IDs
     */
    public ArrayList<Integer> getIdRules() {
        return this.idRules;
    }

    /**
     * Gets the number of rules that apply to the cell.
     *
     * @return the number of rules
     */
    public int getNumberOfRules() {
        return this.idRules.size();
    }

    /**
     * Gets the number of printable rules that apply to the cell.
     *
     * @param rules the list of rules
     * @return the number of printable rules
     */
    public int getNumberOfPrintableRules(ArrayList<Rule> rules) {
        int count = 0;
        for (Integer idRule : this.idRules) {
            if (rules.get(idRule) instanceof BlockRule) {
                count++;
            }
        }
        return count;
    }
}