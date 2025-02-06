package sudoku;

import java.util.ArrayList;

import sudoku.rule.BlockRule;
import sudoku.rule.Rule;

public class Cell {
    private String symbol;
    private final ArrayList<Integer> idRules;

    public Cell(ArrayList<Integer> idRules) {
        this.idRules = idRules;
    }

    public Cell(Integer idRule) {
        this();
        this.idRules.add(idRule);
    }

    public Cell() {
        this.idRules = new ArrayList<>();
    }

    public void addRule(Integer idRule) {
        this.idRules.add(idRule);
    }

    // Maybe we will never use it
    public void deleteRule(Integer idRule) {
        this.idRules.remove(idRule);
    }

    public void insertSymbol(String symbol) {
        if (this.symbol != null) {
            System.err.println("[Cell] A value is already inside of the grid (" + this.symbol + ")");
            return;
        }
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public int getIdRule(int index) {
        if (index < 0 || index >= this.idRules.size()) {
            System.err
                    .println("[Cell] getIdRule index out of range. Index: " + index + ", Size: " + this.idRules.size());
            return -1;
        }
        return this.idRules.get(index);
    }

    public void resetSymbol() {
        this.symbol = null;
    }

    public ArrayList<Integer> getIdRules() {
        return this.idRules;
    }

    public int getNumberOfRules() {
        return this.idRules.size();
    }

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
