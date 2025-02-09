package solvers.backtrack;

import java.util.HashSet;
import java.util.Set;

import solvers.Solver;
import sudoku.Cell;
import sudoku.Grid;
import sudoku.rule.Rule;
import sudoku.Position;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.List;

public class BacktrackOptimized extends Solver {
    private int attempts = 0;
    private Map<Position, Set<String>> possibleValuesCache = new HashMap<>();

    private Set<String> getPossiblePlaysWithCache(Position pos) {
        // return getPossiblePlays(pos);
        return possibleValuesCache.computeIfAbsent(pos, this::getPossiblePlays);
    }

    private void invalidateCache(Position pos) {
        possibleValuesCache.remove(pos);
        // Invalidate cache for related positions
        for (int idRule : grid.getCell(pos).getIdRules()) {
            Rule rule = grid.getRule(idRule);
            for (Position relatedPos : rule.getRulePositions()) {
                possibleValuesCache.remove(relatedPos);
            }
        }
    }

    public BacktrackOptimized(Grid grid) {
        super(grid);
    }

    @Override
    public void solve() {
        this.lastInserts.clear();
        this.historyInserts.clear();
        // backtrack(0, 0);
        backtrackMRV();
    }

    @Override
    public int getNumberOfSolutions() {
        this.lastInserts.clear();
        this.historyInserts.clear();
        Set<Integer> solutions = new HashSet<>();
        return solutions.size();
    }

    private boolean backtrackMRV() {
        attempts++;

        // Find the best cell to fill next
        Position nextPos = findMostConstrainedCell();
        if (nextPos == null) {
            // No empty cells left, puzzle is solved
            return true;
        }

        Set<String> possibleValues = getPossiblePlaysWithCache(nextPos);

        for (String value : possibleValues) {
            if (attempts % 1000 == 0) {
                System.out.println("Attempts: " + attempts + " at " + nextPos + " with " + value);
            }

            insertSymbol(value, nextPos);
            invalidateCache(nextPos);

            if (validateAndPropagate(nextPos) && backtrackMRV()) {
                return true;
            }

            // Rollback
            while (lastInserts.size() > 0 && !lastInserts.get(lastInserts.size() - 1).equals(nextPos)) {
                rollBack();
            }
            if (!lastInserts.isEmpty()) {
                rollBack();
            }
        }

        return false;
    }

    private Position findMostConstrainedCell() {
        Position mostConstrainedPos = null;
        int minPossibilities = Integer.MAX_VALUE;

        Position size = grid.getSize();
        for (int y = 0; y < size.getY(); y++) {
            for (int x = 0; x < size.getX(); x++) {
                Position currentPos = new Position(x, y);
                Cell currentCell = grid.getCell(currentPos);

                // Skip filled or invalid cells
                if (currentCell == null || currentCell.getSymbol() != null) {
                    continue;
                }

                Set<String> possibilities = getPossiblePlaysWithCache(currentPos);
                int numPossibilities = possibilities.size();

                if (numPossibilities == 0) {
                    return currentPos;
                }

                // Update most constrained cell if this one has fewer possibilities
                if (numPossibilities < minPossibilities) {
                    minPossibilities = numPossibilities;
                    mostConstrainedPos = currentPos;
                } else if (numPossibilities == minPossibilities) {
                    // Break ties by choosing the cell with more constraints (rules)
                    if (currentCell.getIdRules().size() >
                        grid.getCell(mostConstrainedPos).getIdRules().size()) {
                        mostConstrainedPos = currentPos;
                    }
                }
            }
        }

        return mostConstrainedPos;
    }

    private boolean validateAndPropagate(Position currentPos) {
        System.out.println("Attmpts " + attempts++);
        // First level: Forward check the current position
        if (!isForwardCheckValid(currentPos)) {
            return false;
        }

        // Second level: Apply naked pairs elimination
        // for (int idRule : grid.getCell(currentPos).getIdRules()) {
        //     Rule rule = grid.getRule(idRule);
        //     eliminateNakedPairs(rule);
        // }

        // Third level: Find hidden singles
        Set<Position> hiddenSinglesPositions = findHiddenSingles(currentPos);

        // Process hidden singles
        for (Position pos : hiddenSinglesPositions) {
            Set<String> possibleValues = getPossiblePlaysWithCache(pos);
            if (possibleValues.size() != 1) {
                continue;
            }

            String value = possibleValues.iterator().next();
            insertSymbol(value, pos);
            invalidateCache(pos);

            // Validate the new insertion
            if (!validateAndPropagate(pos)) {
                return false;
            }
        }

        return true;
    }

    private Set<Position> findHiddenSingles(Position currentPos) {
        Set<Position> hiddenSinglesPositions = new HashSet<>();

        for (int idRule : grid.getCell(currentPos).getIdRules()) {
            Rule rule = grid.getRule(idRule);
            for (Position pos : rule.getRulePositions()) {
                if (!pos.equals(currentPos)) {
                    Cell cell = grid.getCell(pos);
                    if (cell.getSymbol() != null) {
                        continue;
                    }
                    Set<String> possibleValues = getPossiblePlaysWithCache(pos);
                    if (possibleValues.size() == 1) {
                        hiddenSinglesPositions.add(pos);
                    }
                }
            }
        }

        return hiddenSinglesPositions;
    }

    private boolean isForwardCheckValid(Position currentPos) {
        for (int idRule : grid.getCell(currentPos).getIdRules()) {
            Rule rule = grid.getRule(idRule);
            for (Position pos : rule.getRulePositions()) {
                if (!pos.equals(currentPos)) {
                    Cell cell = grid.getCell(pos);
                    if (cell.getSymbol() != null) {
                        continue;
                    }
                    Set<String> possibleValuesOtherCells = getPossiblePlaysWithCache(pos);
                    if (possibleValuesOtherCells.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void eliminateNakedPairs(Rule rule) {
        List<Position> unfilledPositions = rule.getRulePositions().stream()
            .filter(pos -> grid.getCell(pos).getSymbol() == null)
            .collect(Collectors.toList());

        for (int i = 0; i < unfilledPositions.size(); i++) {
            for (int j = i + 1; j < unfilledPositions.size(); j++) {
                Position pos1 = unfilledPositions.get(i);
                Position pos2 = unfilledPositions.get(j);

                Set<String> values1 = getPossiblePlaysWithCache(pos1);
                Set<String> values2 = getPossiblePlaysWithCache(pos2);

                if (values1.equals(values2) && values1.size() == 2) {
                    // Remove these values from other cells in the same rule
                    for (Position pos : unfilledPositions) {
                        if (!pos.equals(pos1) && !pos.equals(pos2)) {
                            // Get the cached possible values and modify them
                            Set<String> possibleValues = getPossiblePlaysWithCache(pos);
                            possibleValues.removeAll(values1);
                            // Update the cache
                            possibleValuesCache.put(pos, possibleValues);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void rollBack() {
        if (this.lastInserts.isEmpty()) {
            return;
        }

        Position lastMovePosition = this.lastInserts.removeLast();
        String lastSymbolInserted = this.grid.getSymbol(lastMovePosition);
        this.grid.resetSymbol(lastMovePosition);
        invalidateCache(lastMovePosition);

        if (!this.historyInserts.containsKey(this.grid)) {
            this.historyInserts.put(this.grid, new HashMap<>());
        }
        if (!this.historyInserts.get(this.grid).containsKey(lastMovePosition)) {
            this.historyInserts.get(this.grid).put(lastMovePosition, new HashSet<>());
        }
        this.historyInserts.get(this.grid).get(lastMovePosition).add(lastSymbolInserted);
    }
}
