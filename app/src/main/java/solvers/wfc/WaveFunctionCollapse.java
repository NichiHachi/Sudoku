package solvers.wfc;

import solvers.Solver;
import sudoku.Grid;
import sudoku.Position;
import sudoku.rule.Rule;

import java.util.*;

public class WaveFunctionCollapse extends Solver {
    int[][] entropy;

    public WaveFunctionCollapse(Grid grid){
        super(grid);
        int sizeX = grid.getSize().getX();
        int sizeY = grid.getSize().getY();
        this.entropy = new int[sizeY][sizeX];
        this.fillEntropy();
    }

    private void fillEntropy(){
        for(int y=0; y<grid.getSize().getY(); y++){
            for(int x=0; x<grid.getSize().getX(); x++){
                entropy[y][x] = this.grid.getPossiblePlays(new Position(x, y)).size();
            }
        }
    }

    @Override
    protected void rollBack(){
        if (this.lastInserts.isEmpty()){
            return;
        }

        Position lastMovePosition = this.lastInserts.removeLast();
        String lastSymbolInserted =  this.grid.getSymbol(lastMovePosition);
        this.grid.resetSymbol(lastMovePosition);

        if(!this.historyInserts.containsKey(this.grid)){
            this.historyInserts.put(this.grid, new HashMap<>());
        }
        if (!this.historyInserts.get(this.grid).containsKey(lastMovePosition)){
            this.historyInserts.get(this.grid).put(lastMovePosition, new HashSet<>());
        }
        this.historyInserts.get(this.grid).get(lastMovePosition).add(lastSymbolInserted);
        this.propagateEntropy(lastSymbolInserted, lastMovePosition, 1);
    }

    @Override
    protected void insertSymbol(String symbol, Position position){
        this.grid.insertSymbol(symbol, position);
        this.lastInserts.add(position);
        this.propagateEntropy(symbol, position, -1);
    }

    public void solve() {
        while (!this.grid.isComplete()) {
            Entropy cellsEntropy = this.getPositionsMinimumEntropy();
            Set<Position> positionsMinimumEntropy = cellsEntropy.getPositionCells();
            this.printEntropy();
            if (positionsMinimumEntropy.isEmpty()) {
                if (!this.lastInserts.isEmpty()) {
                    this.rollBack();
                } else {
                    System.out.println("Impossible to solve... Exiting");
                    break;
                }
            } else {
                if(cellsEntropy.getEntropy() == 0){
                    this.rollBack();
                    continue;
                }
                Position randomPosition = this.chooseRandomPosition(positionsMinimumEntropy);
                Set<String> possiblePlays = this.grid.getPossiblePlays(randomPosition);
                String randomSymbol = this.chooseRandomSymbol(possiblePlays);
                this.insertSymbol(randomSymbol, randomPosition);
            }
            this.grid.print();
        }
        this.grid.print();
    }

    private Entropy getPositionsMinimumEntropy(){
        Entropy cellsEntropy = new Entropy();
        for(int y=0; y<this.grid.getSize().getY(); y++){
            for(int x=0; x<this.grid.getSize().getX(); x++){
                Position position = new Position(x,y);
                System.out.println(position);
                System.out.println(this.grid.getSymbol(position));
                System.out.println(this.entropy[y][x]);
                if(!this.grid.isInsideGrid(position) || this.grid.getSymbol(position) != null) {
                    System.out.println("Yes");
                    continue;
                }
                int alreadyDone = this.getHistoryInsert(position).size();
                cellsEntropy.addCell(this.entropy[y][x] - alreadyDone, position);
            }
        }
        return cellsEntropy;
    }

    private void propagateEntropy(String symbol, Position position, int value){
        Set<Position> positions = new HashSet<>();
        ArrayList<Integer> idRules = this.grid.getCell(position).getIdRules();
        for(int idRule : idRules){
            Rule rule = this.grid.getRule(idRule);
            int indexSymbols = rule.getIndexSymbols();
            if(!this.grid.getSymbols(indexSymbols).contains(symbol)){
                continue;
            }
            positions.addAll(rule.getRulePositions());
        }

        for(Position positionEntropy : positions){
            int x = positionEntropy.getX();
            int y = positionEntropy.getY();
            this.entropy[y][x] = this.entropy[y][x] + value;
        }
    }

    public void printEntropy() {
        for (int y = 0; y < entropy.length; y++) {
            for (int x = 0; x < entropy[y].length; x++) {
                System.out.print(entropy[y][x] + " ");
            }
            System.out.println();
        }
    }
}
