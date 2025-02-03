package solvers;

import sudoku.Grid;
import sudoku.Position;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public abstract class Solver {
    protected Grid grid;
    protected ArrayList<PreviousState> previousStates;
    protected Map<Grid, ArrayList<HistoryMove>> historyMoves;

    protected Solver(Grid grid){
        this.grid = grid;
    }

    public abstract void solve();

    protected void rollBack(PreviousState previousState){
        this.grid.getCell(previousState.position).resetValue();
        for(int i=0; i<previousState.rules.size(); i++){
            int idRule = this.grid.getCell(previousState.position).getIdRule(i);
            //this.grid.addRule(idRule, previousState.value, previousState.rules.get(i));
        }
    }

    protected HistoryMove getHistoryMoveFromPosition(Position position){
        if(!this.historyMoves.containsKey(this.grid)){
            return null;
        }

        for(HistoryMove historyMove : this.historyMoves.get(this.grid)){
            if(historyMove.position.equals(position)){
                return historyMove;
            }
        }

        return null;
    }

    protected ArrayList<Position> getPositionsCompleted(){
        ArrayList<Position> result = new ArrayList<>();
        if(!this.historyMoves.containsKey(this.grid)){
            return result;
        }

        for(HistoryMove historyMove : this.historyMoves.get(this.grid)){
            if(historyMove.isComplete){
                result.add(historyMove.position);
            }
        }
        return result;
    }

    protected String chooseRandomValue(Set<String> possiblePlays){
        Random random = new Random();
        String[] possiblePlaysArray = possiblePlays.toArray(new String[0]);
        int randomIndex = random.nextInt(possiblePlays.size());
        return possiblePlaysArray[randomIndex];
    }
}
