package solvers.wfc;

import solvers.Solver;
import sudoku.Grid;
import sudoku.Position;
import sudoku.rule.Rule;

import java.util.*;

import static java.lang.Thread.sleep;

public class WaveFunctionCollapse extends Solver {
    int[][] entropy;

    public WaveFunctionCollapse(Grid grid){
        super(grid);
        int sizeX = grid.getSize().getX();
        int sizeY = grid.getSize().getY();
        this.entropy = new int[sizeY][sizeX];
    }

    private void fillEntropy(){
        for(int y=0; y<grid.getSize().getY(); y++){
            for(int x=0; x<grid.getSize().getX(); x++){
                Position position = new Position(x, y);
                if(grid.getCell(position) == null || grid.getSymbol(position) != null){
                    this.entropy[y][x] = -1;
                } else {
                    this.entropy[y][x] = this.getPossiblePlays(position).size();
                };
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
        this.propagateEntropy(lastSymbolInserted, lastMovePosition, false);
    }

    @Override
    protected void insertSymbol(String symbol, Position position){
        super.insertSymbol(symbol, position);
        this.propagateEntropy(symbol, position, true);
    }

    @Override
    public void solve() {
        this.lastInserts = new ArrayList<>();
        this.historyInserts = new HashMap<>();
        this.fillEntropy();
        while (!this.grid.isComplete()) {
            Entropy cellsEntropy = this.getPositionsMinimumEntropy();
            Set<Position> positionsMinimumEntropy = cellsEntropy.getPositionCells();
//            this.printEntropy();
            if (positionsMinimumEntropy.isEmpty()) {
                if (!this.lastInserts.isEmpty()) {
                    this.rollBack();
                } else {
                    System.out.println("Impossible to solve... Exiting");
                    break;
                }
            } else {
                if (cellsEntropy.getEntropy() <= 0) {
                    this.rollBack();
                } else {
                    Position randomPosition = this.chooseRandomPosition(positionsMinimumEntropy);
                    Set<String> possiblePlays = this.getPossiblePlays(randomPosition);
                    String randomSymbol = this.chooseRandomSymbol(possiblePlays);
                    this.insertSymbol(randomSymbol, randomPosition);
                }
            }
//            this.grid.print();
//            this.printEntropy();
//            System.out.println((this.lastInserts.getLast()));
//            try {
//                sleep(50);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
//        this.grid.print();
    }

    @Override
    public int getNumberOfSolutions() {
        this.lastInserts = new ArrayList<>();
        this.historyInserts = new HashMap<>();
        this.fillEntropy();
        Set<Integer> solutions = new HashSet<>();
        Entropy cellsEntropy = this.getPositionsMinimumEntropy();
        int entropy = cellsEntropy.getEntropy();

        do {
            Set<Position> positionsMinimumEntropy = cellsEntropy.getPositionCells();
            if (this.grid.isComplete()) {
                solutions.add(grid.hashCode());
                this.rollBack();
            } else if (positionsMinimumEntropy.isEmpty()) {
                this.rollBack();
            } else {
                if (entropy <= 0) {
                    this.rollBack();
                } else {
                    Position randomPosition = this.chooseRandomPosition(positionsMinimumEntropy);
                    Set<String> possiblePlays = this.getPossiblePlays(randomPosition);
                    String randomSymbol = this.chooseRandomSymbol(possiblePlays);
                    this.insertSymbol(randomSymbol, randomPosition);
                }
            }
            cellsEntropy = this.getPositionsMinimumEntropy();
            entropy = cellsEntropy.getEntropy();
        } while (!this.lastInserts.isEmpty() || entropy > 0);

        return solutions.size();
    }

    private Entropy getPositionsMinimumEntropy() {
        Entropy cellsEntropy = new Entropy();
        for (int y = 0; y < this.grid.getSize().getY(); y++) {
            for (int x = 0; x < this.grid.getSize().getX(); x++) {
                Position position = new Position(x, y);
                if (!this.grid.isInsideGrid(position) || this.grid.getSymbol(position) != null) {
                    continue;
                }

                int alreadyDone = this.getHistoryInsert(position).size();

                cellsEntropy.addCell(this.entropy[y][x] - alreadyDone, position);
            }
        }
        return cellsEntropy;
    }

    private void propagateEntropy(String symbol, Position position, boolean isInsert){
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

            this.entropy[y][x] = this.grid.getPossiblePlays(positionEntropy).size();
        }
    }

    public void printEntropy() {
        for (int[] ints : entropy) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
    }
}
