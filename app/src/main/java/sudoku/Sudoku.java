/*
 * This source file was generated by the Gradle 'init' task
 */
package sudoku;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Sudoku {
    ArrayList<Rule> rules;
    ArrayList<Set<Position>> rulesPosition;
    Position absolutePosition;
    Position size;

    public Sudoku(Position size, Position absolutePosition){
        this.size = size;
        this.absolutePosition = absolutePosition;
        this.rules = new ArrayList<>();
        this.rulesPosition = new ArrayList<>();
    }

    public Sudoku(int squareSize, Position absolutePosition){
        this.size = new Position(squareSize);
        this.absolutePosition = absolutePosition;
        this.rules = new ArrayList<>();
        this.rulesPosition = new ArrayList<>();
    }

    public Sudoku(int squareSize, int diagonalAbsolutePosition){
        this.size = new Position(squareSize);
        this.absolutePosition = new Position(diagonalAbsolutePosition);
        this.rules = new ArrayList<>();
        this.rulesPosition = new ArrayList<>();
    }

    public Sudoku(Position size, int diagonalAbsolutePosition){
        this.size = size;
        this.absolutePosition = new Position(diagonalAbsolutePosition);
        this.rules = new ArrayList<>();
        this.rulesPosition = new ArrayList<>();
    }

    public ArrayList<Rule> getRules(){
        return this.rules;
    }

    public ArrayList<Set<Position>> getRulesPositions(){
        return this.rulesPosition;
    }

    public Position getSize() {
        return this.size;
    }

    public Position getMinPosition(){
        return this.absolutePosition;
    }

    public Position getMaxPosition(){
        return this.absolutePosition.add(this.size);
    }

    private boolean isInsideOfSudoku(Position position){
        return position.getX() >= 0 && position.getY() >= 0
                && position.getX() < this.size.getX() && position.getY() < this.size.getY();
    }

    public void add(Rule rule, ArrayList<Position> rulePositions){
        this.rules.add(rule);
        // Changing the position to the absolute position because it is useless to stock it as it is.
        Set<Position> ruleAbsolutePos = new HashSet<>();
        for(Position pos : rulePositions){
            if (!isInsideOfSudoku(pos)){
                System.err.println("Sudoku: The position is outside of the Sudoku, cancel the addition of the rule.");
                return;
            }
            ruleAbsolutePos.add(pos.add(this.absolutePosition));
        }
        this.rulesPosition.add(ruleAbsolutePos);
    }

    public int getNumberRule(){
        return this.rules.size();
    }

    public Rule getRule(int index){
        return this.rules.get(index);
    }

    public Set<Position> getRulePositions(int index){
        return this.rulesPosition.get(index);
    }

    public void remove(Rule rule){
        int index = this.rules.indexOf(rule);
        this.rules.remove(index);
        this.rulesPosition.remove(index);
    }
}
