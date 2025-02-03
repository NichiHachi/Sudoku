package sudoku;

import sudoku.rule.Rule;
import sudoku.sudoku.Sudoku;

import java.util.*;

public class Grid {
    private ArrayList<Rule> rules;
    private Position size;
    private Cell[][] gridCell;
    private final ArrayList<Set<String>> symbols;

    public Grid() {
        this.rules = new ArrayList<>();
        this.symbols = new ArrayList<>();
    }

    public Grid(Builder builder){
        this();

        Position minPos = builder.sudokus.getFirst().getMinPosition();
        Position maxPos = builder.sudokus.getFirst().getMaxPosition();
        for (Sudoku sudoku : builder.sudokus) {
            minPos = minPos.min(sudoku.getMinPosition());
            maxPos = maxPos.max(sudoku.getMaxPosition());
        }
        Position resizeVector = minPos.negative();
        this.size = maxPos.add(resizeVector);

        this.gridCell = new Cell[this.size.getY()][this.size.getX()];

        this.mergeSudokus(builder.sudokus, resizeVector);
        this.initCells();
    }

    public static class Builder {
        private ArrayList<Sudoku> sudokus = new ArrayList<>();

        public Builder addSudoku(Sudoku sudoku) {
            this.sudokus.add(sudoku);
            return this;
        }

        public Builder addSudokus(ArrayList<Sudoku> sudokus) {
            this.sudokus.addAll(sudokus);
            return this;
        }

        public Grid build() {
            return new Grid(this);
        }
    }

    public void mergeSudokus(ArrayList<Sudoku> sudokus, Position resizeVector) {
        for (Sudoku sudoku : sudokus) {
            if(!this.symbols.contains(sudoku.getSymbols())){
                this.symbols.add(sudoku.getSymbols());
            }
            int index = this.symbols.indexOf(sudoku.getSymbols());
            Position offsetSudoku = sudoku.getOffsetPosition().add(resizeVector);

            for (int i=0; i<sudoku.getNumberRule(); i++) {
                Rule rule = sudoku.getRule(i);
                rule.setIndexSymbols(index);
                rule.offsetRepositioning(offsetSudoku);
                if (!this.containRule(rule)) {
                    this.addRule(rule);
                }
            }
        }
    }

    private boolean containRule(Rule rule) {
        for (Rule existingRule : this.rules) {
            for (Position pos : rule.getRulePositions()) {
                if (existingRule.getRulePositions().contains(pos)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void initCells() {
        for(int i=0; i<this.rules.size(); i++){
            Rule rule = this.rules.get(i);
            for(Position position : rule.getRulePositions()){
                int x = position.getX();
                int y = position.getY();
                if(this.gridCell[y][x] == null){
                    this.gridCell[y][x] = new Cell();
                }
                this.gridCell[y][x].addRule(i);
            }
        }
    }

    public Position getSize(){
        return this.size;
    }

    public Cell getCell(Position position){
        return this.gridCell[position.getY()][position.getX()];
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

    public void insertSymbol(String symbol, Position position){
        if(this.canInsertValue(symbol, position)){
            this.handleInsertValue(symbol, position);
        }
    }

    private void handleInsertValue(String symbol, Position position){
        int x = position.getX();
        int y = position.getY();
        this.gridCell[y][x].insertValue(symbol);
    }

    private boolean canInsertValue(String symbol, Position position){
        int x = position.getX();
        int y = position.getY();

        if(!this.isInsideGrid(position)){
            System.err.println("[Grid] Insert outside of a Sudoku");
            return false;
        }

        for(int indexRule : this.gridCell[y][x].getIdRules()){
            Rule rule = this.rules.get(indexRule);

            int indexSymbols = rule.getIndexSymbols();
            Set<String> symbols = this.symbols.get(indexSymbols);
            if(!symbols.contains(symbol)){
                return false;
            }

            for(Position rulePosition : rule.getRulePositions()){
                if(Objects.equals(this.getSymbol(rulePosition), symbol)){
                    return false;
                }
            }
        }

        return true;
    }

    private String getSymbol(Position position){
        int x = position.getX();
        int y = position.getY();
        return this.gridCell[y][x].getValue();
    }

    private boolean isInsideGrid(Position position){
        return position.getX() >= 0 && position.getY() >= 0
                && position.getX() < this.size.getX() && position.getY() < this.size.getY()
                && this.gridCell[position.getY()][position.getX()] != null;
    }

    private void removeCell(Position position){
        int x = position.getX();
        int y = position.getY();
        if(isInsideGrid(position)){
            this.gridCell[y][x] = null;
        }
    }

    public boolean isComplete(){
        for(int y=0; y<this.size.getY(); y++){
            for(int x=0; x<this.size.getX(); x++){
                if(this.gridCell[y][x] != null && this.gridCell[y][x].getValue() == null){
                    return false;
                }
            }
        }
        return true;
    }

    public void playTerminal(){
        Scanner scanner = new Scanner(System.in);
        while(!this.isComplete()) {
            this.print();
            System.out.print("Insert value: ");
            String value = scanner.nextLine();
            System.out.print("Place " + value + " here: ");
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            scanner.nextLine();
            Position position = new Position(x, y);
            this.insertSymbol(value, position);
        }
        this.print();
    }

    public Set<String> getPossiblePlays(Position position){
        int x = position.getX();
        int y = position.getY();
        if(!this.isInsideGrid(position) || this.gridCell[y][x].getValue() != null || this.gridCell[y][x].getIdRules().isEmpty()){
            return new HashSet<>();
        }

        Set<String> intersection = new HashSet<>();

        for(int indexRule: this.gridCell[y][x].getIdRules()){
            Rule rule = this.rules.get(indexRule);
            if(intersection.isEmpty()){
                intersection.addAll(this.symbolLeftRule(rule));
            } else {
                intersection.retainAll(this.symbolLeftRule(rule));
            }
        }
        return intersection;
    }

    public Set<String> symbolLeftRule(Rule rule){
        Set<String> symbols = new HashSet<>(this.symbols.get(rule.getIndexSymbols()));
        for(Position position : rule.getRulePositions()){
            symbols.remove(this.getSymbol(position));
        }
        return symbols;
    }

    public int countPossiblePlays(Position position){
        return this.getPossiblePlays(position).size();
    }

    public void addRule(Rule rule){
        this.rules.add(rule);
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
                "\u001B[38;5;93m",  // Light Purple
                "\u001B[38;5;57m",  // Purple
                "\u001B[38;5;21m",  // Blue
                "\u001B[38;5;51m",  // Cyan
                "\u001B[38;5;46m",  // Green
                "\u001B[38;5;118m"  // Light Green
        };
        return colors[ruleCount % colors.length];
    }


    public ArrayList<Set<String>> getSymbols(){
        return this.symbols;
    }
}