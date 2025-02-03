package solvers.wfc;

import solvers.HistoryMove;
import solvers.PreviousState;
import solvers.Solver;
import sudoku.Grid;
import sudoku.Position;
import java.util.*;

public class WaveFunctionCollapse extends Solver {

    public WaveFunctionCollapse(Grid grid){
        super(grid);
    }

    public void solve() {
        ArrayList<PreviousState> previousStates = new ArrayList<>();
        while (!this.grid.isComplete()) {
            CellsEntropy cellsEntropy = this.getPositionsMinimumEntropy();
            ArrayList<Position> positionsMinimumEntropy = cellsEntropy.getPositionCells();
            positionsMinimumEntropy.removeAll(this.getPositionsCompleted());
            System.out.println(positionsMinimumEntropy);
            if (positionsMinimumEntropy.isEmpty()) {
                if (!previousStates.isEmpty()) {
                    PreviousState previousState = previousStates.removeLast();
                    this.rollBack(previousState);

                    if (!this.historyMoves.containsKey(this.grid)) {
                        this.historyMoves.putIfAbsent(this.grid, new ArrayList<>());
                    }

                    HistoryMove historyMove = this.getHistoryMoveFromPosition(previousState.position);
                    if (historyMove == null) {
                        HistoryMove hm = new HistoryMove(previousState.value, previousState.position);
                        this.historyMoves.get(this.grid).add(hm);
                    } else {
                        historyMove.add(previousState.value);
                    }

                    historyMove = this.getHistoryMoveFromPosition(previousState.position);
                    if (historyMove != null && historyMove.values.size() == this.grid.getPossiblePlays(previousState.position).size()) {
                        historyMove.completed();
                    }

                } else {
                    System.out.println("Impossible to solve... Exiting");
                    break;
                }
            } else {
                Position randomPosition = this.chooseRandomCell(positionsMinimumEntropy);
                Set<String> possiblePlays = this.grid.getPossiblePlays(randomPosition);
                HistoryMove historyMove = this.getHistoryMoveFromPosition(randomPosition);
                if (historyMove != null) {
                    possiblePlays.removeAll(historyMove.values);
                }
                if (!possiblePlays.isEmpty()) {
                    String randomValue = this.chooseRandomValue(possiblePlays);
                    // previousStates.add(new PreviousState(randomPosition, randomValue, this.grid.getRules(randomPosition, randomValue)));
                    this.grid.insertSymbol(randomValue, randomPosition);
                }
            }
            this.grid.print();
            System.out.println(this.grid.hashCode());
            System.out.println(previousStates.size());
        }
        this.grid.print();
    }

    private CellsEntropy getPositionsMinimumEntropy(){
        CellsEntropy cellsEntropy = new CellsEntropy();
        for(int y=0; y<this.grid.getSize().getY(); y++){
            for(int x=0; x<this.grid.getSize().getX(); x++){
                Position position = new Position(x,y);
                if(grid.countPossiblePlays(position) > 0) {
                    cellsEntropy.addCell(grid.countPossiblePlays(position), position);
                }
            }
        }
        return cellsEntropy;
    }

    private Position chooseRandomCell(ArrayList<Position> cellsPosition) {
        if (cellsPosition.isEmpty()) {
            throw new IllegalArgumentException("cellsPosition list must not be empty");
        }
        Random random = new Random();
        int randomIndex = random.nextInt(cellsPosition.size());
        return cellsPosition.get(randomIndex);
    }

    public Grid getGrid(){
        return this.grid;
    }
}
