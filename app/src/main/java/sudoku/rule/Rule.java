package sudoku.rule;

import java.util.HashSet;
import java.util.Set;

import sudoku.Position;

public class Rule {
    private Set<Position> rulePositions;
    private int indexSymbols;

    public Rule(){
        this.rulePositions = new HashSet<>();
    }

    Rule(Position position){
        this();
        this.add(position);
    }

    public Rule(Set<Position> positions){
        this();
        this.rulePositions.addAll(positions);
    }

    public Set<Position> getRulePositions(){
        return this.rulePositions;
    }

    public void add(Position position){
        this.rulePositions.add(position);
    }

    public void offsetRepositioning(Position offset) {
        Set<Position> updatedPositions = new HashSet<>();
        for (Position pos : this.rulePositions) {
            pos.addi(offset);
            updatedPositions.add(pos);
        }
        this.rulePositions = updatedPositions;
    }

    public void setIndexSymbols(int indexSymbols) {
        this.indexSymbols = indexSymbols;
    }

    public int getIndexSymbols() {
        return this.indexSymbols;
    }

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
