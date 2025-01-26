package solvers.wfc;

import solvers.HistoryMove;
import solvers.PreviousState;
import sudoku.Grid;
import sudoku.Position;
import java.util.*;

public class WaveFunctionCollapse {
    private Grid grid;
    private Map<Grid, ArrayList<HistoryMove>> states;

    public WaveFunctionCollapse(Grid grid){
        this.grid = grid;
        this.states = new HashMap<>();
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
                    PreviousState previousState = previousStates.remove(previousStates.size() - 1);
                    this.rollBack(previousState);

                    if (!this.states.containsKey(this.grid)) {
                        this.states.putIfAbsent(this.grid, new ArrayList<>());
                    }

                    HistoryMove historyMove = this.getHistoryMoveFromPosition(previousState.position);
                    if (historyMove == null) {
                        HistoryMove hm = new HistoryMove(previousState.value, previousState.position);
                        this.states.get(this.grid).add(hm);
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
                    previousStates.add(new PreviousState(randomPosition, randomValue, this.grid.getRules(randomPosition, randomValue)));
                    this.grid.insertValue(randomValue, randomPosition);
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

    private String chooseRandomValue(Set<String> possiblePlays){
        Random random = new Random();
        String[] possiblePlaysArray = possiblePlays.toArray(new String[0]);
        int randomIndex = random.nextInt(possiblePlays.size());
        return possiblePlaysArray[randomIndex];
    }

    public Grid getGrid(){
        return this.grid;
    }

    private void rollBack(PreviousState previousState){
        this.grid.getCell(previousState.position).resetValue();
        for(int i=0; i<previousState.rules.size(); i++){
            int idRule = this.grid.getCell(previousState.position).getIdRule(i);
            this.grid.addRule(idRule, previousState.value, previousState.rules.get(i));
        }
    }

    private HistoryMove getHistoryMoveFromPosition(Position position){
        if(!this.states.containsKey(this.grid)){
            System.out.println("Hmmm");
            return null;
        }

        for(HistoryMove historyMove : this.states.get(this.grid)){
            if(historyMove.position.equals(position)){
                return historyMove;
            }
        }

        return null;
    }

    private ArrayList<Position> getPositionsCompleted(){
        ArrayList<Position> result = new ArrayList<>();
        if(!this.states.containsKey(this.grid)){
            return result;
        }

        for(HistoryMove historyMove : this.states.get(this.grid)){
            if(historyMove.isComplete){
                result.add(historyMove.position);
            }
        }
        return result;
    }
}
