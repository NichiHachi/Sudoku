package solvers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import sudoku.Grid;
import sudoku.Position;

public abstract class Solver {
    protected Grid grid;
    protected ArrayList<Position> lastInserts;
    protected Map<Grid, Map<Position, Set<String>>> historyInserts;

    protected Solver(Grid grid) {
        this.grid = grid;
        lastInserts = new ArrayList<>();
        historyInserts = new HashMap<>();
    }

    public abstract void solve();

    public abstract int getNumberOfSolutions() throws CloneNotSupportedException;

    protected void rollBack() {
        if (this.lastInserts.isEmpty()) {
            return;
        }

        Position lastMovePosition = this.lastInserts.removeLast();
        String lastSymbolInserted = this.grid.getSymbol(lastMovePosition);
        this.grid.resetSymbol(lastMovePosition);

        if (!this.historyInserts.containsKey(this.grid)) {
            this.historyInserts.put(this.grid, new HashMap<>());
        }
        if (!this.historyInserts.get(this.grid).containsKey(lastMovePosition)) {
            this.historyInserts.get(this.grid).put(lastMovePosition, new HashSet<>());
        }
        this.historyInserts.get(this.grid).get(lastMovePosition).add(lastSymbolInserted);
    }

    protected void insertSymbol(String symbol, Position position) {
        this.grid.insertSymbol(symbol, position);
        this.lastInserts.add(position);
    }

    protected Set<String> getHistoryInsert(Position position) {
        if (!this.historyInserts.containsKey(this.grid)) {
            return new HashSet<>();
        }

        for (Map.Entry<Position, Set<String>> entry : this.historyInserts.get(this.grid).entrySet()) {
            if (entry.getKey().equals(position)) {
                return entry.getValue();
            }
        }

        return new HashSet<>();
    }

    protected Set<String> getPossiblePlays(Position position) {
        Set<String> possiblePlays = this.grid.getPossiblePlays(position);
        possiblePlays.removeAll(getHistoryInsert(position));
        return possiblePlays;
    }

    protected String chooseRandomSymbol(Set<String> possiblePlays) {
        Random random = new Random();
        String[] possiblePlaysArray = possiblePlays.toArray(new String[0]);
        int randomIndex = random.nextInt(possiblePlays.size());
        return possiblePlaysArray[randomIndex];
    }

    protected Position chooseRandomPosition(Set<Position> positions) {
        if (positions.isEmpty()) {
            throw new IllegalArgumentException("[Solver] Positions must not be empty");
        }
        Random random = new Random();
        List<Position> positionList = new ArrayList<>(positions);
        int randomIndex = random.nextInt(positionList.size());
        return positionList.get(randomIndex);
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }
}
