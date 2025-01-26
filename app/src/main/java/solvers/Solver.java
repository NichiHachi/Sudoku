package solvers;

import sudoku.Grid;

import java.util.ArrayList;
import java.util.Map;

public abstract class Solver {
    protected Grid grid;
    protected ArrayList<PreviousState> previousStates;
    private Map<Grid, ArrayList<HistoryMove>> states;

    Solver(Grid grid){
        this.grid = grid;
    }

    public abstract void solve();


}
