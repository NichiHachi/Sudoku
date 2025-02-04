package sudoku.rule;

import sudoku.Position;

import java.util.HashSet;
import java.util.Set;

public class Rule {
    private Set<Position> rulePositions;
    private int indexSymbols;

    Rule(){
        this.rulePositions = new HashSet<>();
    }

    Rule(Position position){
        this();
        this.add(position);
    }

    Rule(Set<Position> positions){
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
