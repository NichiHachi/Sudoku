package sudoku.rule;

import java.util.HashSet;
import java.util.Set;

import sudoku.Position;

/**
 * The Rule class represents a rule in a Sudoku puzzle.
 * A rule consists of a set of positions that must adhere to certain
 * constraints.
 */
public class Rule {
    private Set<Position> rulePositions;
    private int indexSymbols;

    /**
     * Constructs an empty Rule.
     */
    public Rule() {
        this.rulePositions = new HashSet<>();
    }

    /**
     * Constructs a Rule with a single position.
     *
     * @param position the position to add to the rule
     */
    Rule(Position position) {
        this();
        this.add(position);
    }

    /**
     * Constructs a Rule with a set of positions.
     *
     * @param positions the set of positions to add to the rule
     */
    public Rule(Set<Position> positions) {
        this();
        this.rulePositions.addAll(positions);
    }

    /**
     * Gets the set of positions that make up the rule.
     *
     * @return the set of positions
     */
    public Set<Position> getRulePositions() {
        return this.rulePositions;
    }

    /**
     * Adds a position to the rule.
     *
     * @param position the position to add
     */
    public void add(Position position) {
        this.rulePositions.add(position);
    }

    /**
     * Repositions the rule by an offset.
     *
     * @param offset the offset to reposition the rule by
     */
    public void offsetRepositioning(Position offset) {
        Set<Position> updatedPositions = new HashSet<>();
        for (Position pos : this.rulePositions) {
            pos.addi(offset);
            updatedPositions.add(pos);
        }
        this.rulePositions = updatedPositions;
    }

    /**
     * Sets the index of the symbols associated with the rule.
     *
     * @param indexSymbols the index of the symbols
     */
    public void setIndexSymbols(int indexSymbols) {
        this.indexSymbols = indexSymbols;
    }

    /**
     * Gets the index of the symbols associated with the rule.
     *
     * @return the index of the symbols
     */
    public int getIndexSymbols() {
        return this.indexSymbols;
    }

    /**
     * Returns a string representation of the rule.
     *
     * @return a string representation of the rule
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("Rule Positions: [");
        for (Position pos : rulePositions) {
            string.append(pos.toString()).append(", ");
        }
        if (!rulePositions.isEmpty()) {
            string.setLength(string.length() - 2);
        }
        string.append("]");
        return string.toString();
    }
}