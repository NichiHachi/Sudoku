/*
 * This source file was generated by the Gradle 'init' task
 */
package sudoku.sudoku;

import sudoku.Position;
import sudoku.rule.ColumnRule;
import sudoku.rule.RowRule;
import sudoku.rule.Rule;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Sudoku {
    private int size;
    private Position offsetPosition;
    private ArrayList<Rule> rules;
    private Set<String> symbols;

    public Sudoku(int size, Set<String> symbols, Position offsetPosition){
        if(symbols.size() < size){
            System.err.println("[Sudoku] There are less value than there is columns and lines on the Sudoku");
            return;
        }

        this.size = size;
        this.offsetPosition = offsetPosition;
        this.rules = new ArrayList<>();
        this.symbols = symbols;
    }

    public Sudoku(int size, Set<String> symbols, int offset){
        this(size, symbols, new Position(offset));
    }

    public Sudoku(int size, Set<String> symbols) {
        this(size, symbols, new Position(0));
    }

    public Sudoku(int size, Set<String> symbols, ArrayList<Rule> rules, Position diagonalOffsetPosition){
        this(size, symbols, diagonalOffsetPosition);

        for (Rule rule : rules) {
            this.add(rule);
        }

        this.addColumnRules();
        this.addRowRules();
    }

    public Sudoku(int size, Set<String> symbols, ArrayList<Rule> rules, int offset){
        this(size, symbols, rules, new Position(offset));
    }

    public Sudoku(int size, Set<String> symbols, ArrayList<Rule> rules){
        this(size, symbols, rules, new Position(0));
    }

    public ArrayList<Rule> getRules(){
        return this.rules;
    }

    public int getSize() {
        return this.size;
    }

    public Position getMinPosition(){
        return this.offsetPosition;
    }

    public Position getMaxPosition(){
        return this.offsetPosition.add(this.size);
    }

    public Position getOffsetPosition(){
        return this.offsetPosition;
    }

    private boolean isInsideOfSudoku(Position position){
        return position.getX() >= 0 && position.getY() >= 0
                && position.getX() < this.size && position.getY() < this.size;
    }

    public void add(Rule rule){
        for(Position pos : rule.getRulePositions()){
            if (!isInsideOfSudoku(pos)){
                System.err.println("Sudoku: The rule is outside of the Sudoku, cancel the addition of the rule.");
                return;
            }
        }
        this.rules.add(rule);
    }

    public int getNumberRule(){
        return this.rules.size();
    }

    public Rule getRule(int index){
        return this.rules.get(index);
    }

    public void remove(Rule rule){
        this.rules.remove(rule);
    }

    public void addColumnRules(){
        for(int x=0; x<this.size; x++){
            this.add(new ColumnRule(new Position(x,0), this.size));
        }
    }

    public void addRowRules(){
        for(int y=0; y<this.size; y++){
            this.add(new RowRule(new Position(0,y), this.size));
        }
    }

    public Set<String> getSymbols(){
        return this.symbols;
    }
}
