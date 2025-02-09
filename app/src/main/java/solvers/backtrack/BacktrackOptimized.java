package solvers.backtrack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solvers.Solver;
import sudoku.Cell;
import sudoku.Grid;
import sudoku.Position;
import sudoku.rule.Rule;
import utils.Colors;

public class BacktrackOptimized extends Solver {

    private static final Logger logger = LoggerFactory.getLogger(
        BacktrackOptimized.class
    );

    private int attempts = 0;
    private Map<Position, Set<String>> possibleValuesCache = new HashMap<>();

    public BacktrackOptimized(Grid grid) {
        super(grid);
        logger.info(
            Colors.GREEN +
            "Starting BacktrackOptimized solver..." +
            Colors.RESET
        );
    }

    @Override
    public void solve() {
        this.lastInserts.clear();
        this.historyInserts.clear();
        backtrackMinimumRemainingValues();
    }

    @Override
    public int getNumberOfSolutions() {
        this.lastInserts.clear();
        this.historyInserts.clear();
        Set<Integer> solutions = new HashSet<>();
        return solutions.size();
    }

    private boolean backtrackMinimumRemainingValues() {
        attempts++;
        logger.debug(Colors.INFO_COLOR + "Attempt #" + attempts + Colors.RESET);

        Position nextPos = findMostConstrainedCell();
        if (nextPos == null) {
            logger.info(
                Colors.SUCCESS_COLOR +
                "Solution found after " +
                attempts +
                " attempts!" +
                Colors.RESET
            );
            return true;
        }

        Set<String> possibleValues = getPossiblePlaysWithCache(nextPos);
        logger.debug(
            Colors.DEBUG_COLOR +
            "-> Analyzing position " +
            Colors.HIGHLIGHT_COLOR +
            nextPos +
            Colors.DEBUG_COLOR +
            " | Possible values: " +
            Colors.HIGHLIGHT_COLOR +
            possibleValues +
            Colors.RESET
        );

        for (String value : possibleValues) {
            logger.debug(
                Colors.INFO_COLOR +
                "--> Trying value " +
                Colors.HIGHLIGHT_COLOR +
                value +
                Colors.INFO_COLOR +
                " at position " +
                Colors.HIGHLIGHT_COLOR +
                nextPos +
                Colors.INFO_COLOR +
                " (Attempt #" +
                attempts +
                ")" +
                Colors.RESET
            );

            insertSymbol(value, nextPos);
            invalidateCache(nextPos);

            if (validateAndPropagate(nextPos)) {
                logger.debug(
                    Colors.SUCCESS_COLOR +
                    "Valid insertion: " +
                    value +
                    " at " +
                    nextPos +
                    Colors.RESET
                );
                if (backtrackMinimumRemainingValues()) {
                    return true;
                }
            } else {
                logger.debug(
                    Colors.WARNING_COLOR +
                    "Invalid insertion: " +
                    value +
                    " at " +
                    nextPos +
                    " | Constraint violation detected" +
                    Colors.RESET
                );
            }

            // Rollback
            logger.debug(
                Colors.WARNING_COLOR +
                "Rolling back from position " +
                nextPos +
                " | Value: " +
                value +
                Colors.RESET
            );
            while (
                lastInserts.size() > 0 &&
                !lastInserts.get(lastInserts.size() - 1).equals(nextPos)
            ) {
                rollBack();
            }
            if (!lastInserts.isEmpty()) {
                rollBack();
            }
        }
        logger.debug(
            Colors.ERROR_COLOR +
            "Dead end at " +
            nextPos +
            " | No valid values remain" +
            Colors.RESET
        );
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

                Set<String> possibilities = getPossiblePlaysWithCache(
                    currentPos
                );
                int numPossibilities = possibilities.size();

                if (numPossibilities == 0) {
                    logger.debug(
                        "Found cell with no possibilities at " + currentPos
                    );
                    return currentPos;
                }

                // Update most constrained cell if this one has fewer possibilities
                if (numPossibilities < minPossibilities) {
                    minPossibilities = numPossibilities;
                    mostConstrainedPos = currentPos;
                    logger.debug(
                        "New most constrained cell found at " +
                        currentPos +
                        " with " +
                        numPossibilities +
                        " possibilities"
                    );
                } else if (numPossibilities == minPossibilities) {
                    // Break ties by choosing the cell with more constraints (rules)
                    if (
                        currentCell.getIdRules().size() >
                        grid.getCell(mostConstrainedPos).getIdRules().size()
                    ) {
                        mostConstrainedPos = currentPos;
                    }
                }
            }
        }

        return mostConstrainedPos;
    }

    private boolean validateAndPropagate(Position currentPos) {
        // First level: Forward check the current position
        if (!isForwardCheckValid(currentPos)) {
            logger.debug(
                Colors.ERROR_COLOR +
                "Forward check failed at " +
                currentPos +
                " | Causes domain wipeout in neighboring cells" +
                Colors.RESET
            );
            return false;
        }

        // Second level: Apply naked pairs elimination (not efficient for small grids)
        // for (int idRule : grid.getCell(currentPos).getIdRules()) {
        //     Rule rule = grid.getRule(idRule);
        //     eliminateNakedPairs(rule);
        // }

        // Third level: Find hidden singles
        Set<Position> hiddenSinglesPositions = findHiddenSingles(currentPos);
        if (!hiddenSinglesPositions.isEmpty()) {
            logger.debug(
                Colors.INFO_COLOR +
                "! Found " +
                hiddenSinglesPositions.size() +
                " hidden singles to propagate from " +
                currentPos +
                Colors.RESET
            );
        }

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
                    Set<String> possibleValuesOtherCells =
                        getPossiblePlaysWithCache(pos);
                    if (possibleValuesOtherCells.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void eliminateNakedPairs(Rule rule) {
        List<Position> unfilledPositions = rule
            .getRulePositions()
            .stream()
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
                            Set<String> possibleValues =
                                getPossiblePlaysWithCache(pos);
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
            this.historyInserts.get(this.grid).put(
                    lastMovePosition,
                    new HashSet<>()
                );
        }
        this.historyInserts.get(this.grid)
            .get(lastMovePosition)
            .add(lastSymbolInserted);
    }

    private Set<String> getPossiblePlaysWithCache(Position pos) {
        return possibleValuesCache.computeIfAbsent(pos, this::getPossiblePlays);
    }

    private void invalidateCache(Position pos) {
        possibleValuesCache.remove(pos);
        Set<Position> invalidatedPositions = new HashSet<>();

        for (int idRule : grid.getCell(pos).getIdRules()) {
            Rule rule = grid.getRule(idRule);
            invalidatedPositions.addAll(rule.getRulePositions());
        }

        invalidatedPositions.forEach(p -> possibleValuesCache.remove(p));

        logger.debug(
            Colors.DEBUG_COLOR +
            "Cache invalidated for position " +
            pos +
            " and " +
            (invalidatedPositions.size() - 1) +
            " related positions" +
            Colors.RESET
        );
    }
}
