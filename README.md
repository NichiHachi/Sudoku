# Sudoku

Bulid the project :
```bash
./gradlew build
```

Run the project :
```bash
./gradlew runGraphic // Run the project in a graphical interface without any prints
./gradlew runGraphic --args="--info" // Run the project in a graphical interface with minimal information
./gradlew runGraphic --args="--debug" // Run the project in a graphical interface with steps by steps prints
./gradlew runTerminal --console=plain -q // Run the project in a terminal
```

Documentation :
```bash
./gradlew javadoc
```

## Wave Function Collapse

This solver is based on [Maxim Gumin's work](https://github.com/mxgmn/WaveFunctionCollapse)

1) **Check if the grid is complete** : The algorithm starts by checking if the grid is complete, if so, the algorithm terminates.
2) **Get positions with minimum entropy** : The algorithm calculates the entropy for each cell in the grid and identifies the positions with the minimum entropy using the `getPositionsMinimumEntropy()` method.
3) **Handle empty positions** : If there are no positions with minimum entropy, the algorithm rolls back the last move using the `rollBack()` method. If the rollback is not possible, the algorithm terminates.
4) **Handle zero or negative entropy** : If the minimum entropy is zero or negative, the algorithm rolls back the last move.
5) **Choose a random position and symbol** : The algorithm selects a random position from the positions with minimum entropy and then chooses a random symbol from the possible plays for that position.
6) **Insert the symbol** : The chosen symbol is inserted into the selected position and the entropy is propagated to update the grid's state.
7) **Repeat** : The algorithm repeats the above steps until the grid is complete.
```mermaid
---
title: Diagramme d'activité de WaveFunctionCollapseBacktracking
---
stateDiagram-v2
    [*] --> Initialiser
    Initialiser --> RemplirEntropie
    RemplirEntropie --> VérifierComplétion
    VérifierComplétion --> [*] : La grille est résolue
    VérifierComplétion --> ObtenirPositionsMinimaleEntropie : La grille n'est pas résolue
    ObtenirPositionsMinimaleEntropie --> VérifierPositions
    VérifierPositions --> Rollback : Pas de position de libre
    VérifierPositions --> VérifierEntropie
    VérifierEntropie --> Rollback : L'entropie minimal est inférieur ou égal à 0
    VérifierEntropie --> ChoisirPositionAléatoire
    ChoisirPositionAléatoire --> ChoisirSymboleAléatoire
    ChoisirSymboleAléatoire --> InsérerSymbole
    InsérerSymbole --> PropagerEntropie
    PropagerEntropie --> VérifierComplétion
    Rollback --> PropagerEntropie
```

## Backtrack (Optimized)

This solver implements an optimized version of the traditional backtracking algorithm with several performance enhancements including:
- forward checking
- most constrained variable selection
- constraint propagation
- caching

1) **Find Most Constrained Cell**: The algorithm identifies the cell with the fewest possible valid values (Minimum Remaining Values heuristic). In case of ties, it selects the cell with more constraints.
2) **Get Possible Values**: For the selected cell, determine all valid values that satisfy the Sudoku constraints, using a cache to avoid redundant calculations.
3) **Try Values and Validate**:
   - For each possible value, insert it into the cell
   - Perform forward checking to ensure no neighboring cells are left with no valid options
   - Propagate constraints by identifying and filling "hidden singles" (cells that have only one possible value)
   - If the insertion is valid, recursively continue to the next most constrained cell
4) **Handle Invalid States**:
   - If a constraint violation is detected, rollback the last move
   - If no valid values remain for a cell, backtrack to the previous decision
6) **Repeat**: Continue until either a solution is found or all possibilities are exhausted


## Project structure
```
sudoku/
│
├── README.md
├── build/
├── .gitignore
├── LICENSE
└── app
    └── src  
        └── main  
            └── java  
                ├── solvers  
                │   ├── backtrack  
                │   │   ├── Backtrack.java  
                │   │   ├── BacktrackOptimized.java  
                │   ├── wfc  
                │   │   ├── Entropy.java  
                │   │   ├── WaveFunctionCollapse.java  
                │   │   ├── Solver.java  
                ├── sudoku  
                │   ├── configuration  
                │   │   ├── SudokuImporter.java  
                │   │   ├── SudokuSaver.java  
                │   ├── graphic  
                │   │   ├── GridGraphic.java  
                │   │   ├── MainGraphic.java  
                │   ├── rule  
                │   │   ├── BlockRule.java  
                │   │   ├── ColumnRule.java  
                │   │   ├── RowRule.java  
                │   │   ├── Rule.java  
                │   ├── sudoku  
                │   │   ├── Sudoku.java  
                │   │   ├── SudokuClassic.java  
                │   ├── terminal  
                │   │   ├── MainTerminal.java  
                │   │   ├── PlayTerminal.java  
                ├── Cell.java  
                ├── GenerateSudoku.java  
                ├── Grid.java  
                ├── Main.java  
                └── Position.java  
            test  
            └── java  
                └── sudoku  
                    ├── rules  
                    │   ├── BlockRuleTest.java  
                    │   ├── ColumnRuleTest.java  
                    │   ├── RowRuleTest.java  
                    │   ├── RuleTest.java  
                    ├── sudokus  
                    │   ├── SudokuClassicTest.java  
                    │   ├── SudokuTest.java  
                    ├── CellTest.java  
                    ├── GridTest.java  
                    ├── PositionTest.java 
```

```mermaid
classDiagram
    direction BT
    class BlockRule {
        + BlockRule()
        + BlockRule(Position, Position)
    }
    class Builder {
        + Builder()
        + addSudoku(Sudoku) Builder
        + build() Grid
        + addSudokus(ArrayList~Sudoku~) Builder
    }
    class Cell {
        + Cell()
        + Cell(Integer)
        + Cell(ArrayList~Integer~)
        - ArrayList~Integer~ idRules
        - String symbol
        + getIdRule(int) int
        + insertSymbol(String) void
        + deleteRule(Integer) void
        + addRule(Integer) void
        + resetSymbol() void
        ArrayList~Integer~ idRules
        int numberOfRules
        String symbol
    }
    class ColumnRule {
        + ColumnRule()
        + ColumnRule(Position, int)
        + ColumnRule(Position, Position)
    }
    class Entropy {
        + Entropy(int, Position)
        + Entropy()
        - int entropy
        - Set~Position~ positionCells
        + merge(Entropy) void
        + addCell(int, Position) void
        int entropy
        Set~Position~ positionCells
    }
    class Grid {
        + Grid(Builder)
        + Grid()
        - ArrayList~Rule~ rules
        - ArrayList~Set~String~~ symbols
        - Position size
        + isInsideGrid(Position) boolean
        - removeCell(Position) void
        + playTerminal() void
        - canInsertValue(String, Position) boolean
        - handleInsertValue(String, Position) void
        + initCells() void
        + print() void
        + insertSymbol(String, Position) void
        + getPossiblePlays(Position) Set~String~
        + getSymbol(Position) String
        + countPossiblePlays(Position) int
        + getCell(Position) Cell
        - getColor(int) String
        + addRule(Rule) void
        + hashCode() int
        + mergeSudokus(ArrayList~Sudoku~, Position) void
        - containRule(Rule, int) boolean
        + symbolUsed(Rule) Set~String~
        + getSymbols(int) Set~String~
        + resetSymbol(Position) void
        + getRule(int) Rule
        Position size
        ArrayList~Rule~ rules
        boolean complete
        ArrayList~Set~String~~ symbols
    }
    class Main {
        + Main()
        + main(String[]) void
    }
    class Position {
        + Position(int, int)
        + Position(int)
        - int x
        - int y
        + addYi(int) void
        + add(Position) Position
        + hashCode() int
        + max(Position) Position
        + min(Position) Position
        + add(int) Position
        + toString() String
        + addi(Position) void
        + addY(int) Position
        + addXi(int) void
        + negative() Position
        + equals(Object) boolean
        + addX(int) Position
        int x
        int y
    }
    class RowRule {
        + RowRule(Position, int)
        + RowRule(Position, Position)
        + RowRule()
    }
    class Rule {
        ~ Rule()
        ~ Rule(Position)
        ~ Rule(Set~Position~)
        - int indexSymbols
        - Set~Position~ rulePositions
        + toString() String
        + add(Position) void
        + offsetRepositioning(Position) void
        int indexSymbols
        Set~Position~ rulePositions
    }
    class Solver {
        # Solver(Grid)
        # chooseRandomSymbol(Set~String~) String
        # rollBack() void
        + solve() void
        # getPossiblePlays(Position) Set~String~
        # insertSymbol(String, Position) void
        # getHistoryInsert(Position) Set~String~
        # chooseRandomPosition(Set~Position~) Position
    }
    class Sudoku {
        + Sudoku(int, Set~String~, ArrayList~Rule~, Position)
        + Sudoku(int, Set~String~, Position)
        + Sudoku(int, Set~String~, ArrayList~Rule~)
        + Sudoku(int, Set~String~, ArrayList~Rule~, int)
        + Sudoku(int, Set~String~)
        + Sudoku(int, Set~String~, int)
        # Set~String~ symbols
        # Position offsetPosition
        # ArrayList~Rule~ rules
        # int size
        + remove(Rule) void
        + addColumnRules() void
        + getRule(int) Rule
        - isInsideOfSudoku(Position) boolean
        + add(Rule) void
        + addRowRules() void
        Position minPosition
        Set~String~ symbols
        int size
        Position offsetPosition
        Position maxPosition
        ArrayList~Rule~ rules
        int numberRule
    }
    class SudokuClassic {
        + SudokuClassic(int, Position)
        + SudokuClassic(int)
        + SudokuClassic(int, int)
        - generateSymbols(int) Set~String~
    }
    class WaveFunctionCollapse {
        + WaveFunctionCollapse(Grid)
        # insertSymbol(String, Position) void
        + solve() void
        - fillEntropy() void
        # rollBack() void
        + printEntropy() void
        - propagateEntropy(String, Position, boolean) void
        Entropy positionsMinimumEntropy
    }

    BlockRule  ..>  Position : «create»
    BlockRule  -->  Rule
    Grid  -->  Builder
    Builder  ..>  Grid : «create»
    Builder "1" *--> "sudokus *" Sudoku
    ColumnRule  ..>  Position : «create»
    ColumnRule  -->  Rule
    Entropy "1" *--> "positionCells *" Position
    Grid  ..>  Cell : «create»
    Grid "1" *--> "gridCell *" Cell
    Grid "1" *--> "size 1" Position
    Grid  ..>  Position : «create»
    Grid "1" *--> "rules *" Rule
    Main  ..>  Builder : «create»
    Main  ..>  SudokuClassic : «create»
    Main  ..>  WaveFunctionCollapse : «create»
    RowRule  ..>  Position : «create»
    RowRule  -->  Rule
    Rule "1" *--> "rulePositions *" Position
    Solver "1" *--> "historyInserts *" Grid
    Solver "1" *--> "lastInserts *" Position
    Sudoku  ..>  ColumnRule : «create»
    Sudoku "1" *--> "offsetPosition 1" Position
    Sudoku  ..>  Position : «create»
    Sudoku  ..>  RowRule : «create»
    Sudoku "1" *--> "rules *" Rule
    SudokuClassic  ..>  BlockRule : «create»
    SudokuClassic  ..>  Position : «create»
    SudokuClassic  -->  Sudoku
    WaveFunctionCollapse  ..>  Entropy : «create»
    WaveFunctionCollapse  ..>  Position : «create»
    WaveFunctionCollapse  -->  Solver
```
