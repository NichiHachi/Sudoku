# Sudoku

Bulid the project :
```bash
./gradlew build
```

Run the project :
```bash
./gradlew run --console=plain -q
```

## Project structure

```
sudoku/
│
├── README.md
├── app/src
│   ├── main/java/sudoku/
│   │   ├── Grid.java
│ [gradlew](gradlew)  │   ├── Cell.java
│   │   ├── Constraint.java
│   │   ├── Zone.java
│   │   ├── Rule.java
│   │   ├── Element.java
│   │   └── Main.java
│   │
│   └── test/java/sudoku/
│       ├── GridTest.java
│       ├── CellTest.java
│       ├── ConstraintTest.java
│       ├── ZoneTest.java
│       ├── RuleTest.java
│       └── ElementTest.java
│
├── build/
├── .gitignore
└── LICENSE
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
        + addSudokus(ArrayList~Sudoku~) Builder
        + build() Grid
        + addSudoku(Sudoku) Builder
    }
    class Cell {
        + Cell(ArrayList~Integer~)
        + Cell(Integer)
        + Cell()
        - String value
        - ArrayList~Integer~ idRules
        + insertValue(String) void
        + deleteRule(Integer) void
        + addRule(Integer) void
        + getIdRule(int) int
        + resetValue() void
        ArrayList~Integer~ idRules
        int numberOfRules
        String value
    }
    class CellsEntropy {
        + CellsEntropy(int, Position)
        + CellsEntropy()
        - int entropy
        - ArrayList~Position~ positionCells
        + merge(CellsEntropy) void
        + addCell(int, Position) void
        int entropy
        ArrayList~Position~ positionCells
    }
    class ColumnRule {
        + ColumnRule()
        + ColumnRule(Position, int)
        + ColumnRule(Position, Position)
    }
    class Grid {
        + Grid()
        + Grid(Builder)
        - ArrayList~Set~String~~ symbols
        - Position size
        + getCell(Position) Cell
        - handleInsertValue(String, Position) void
        + playTerminal() void
        - getSymbol(Position) String
        + insertSymbol(String, Position) void
        - canInsertValue(String, Position) boolean
        + mergeSudokus(ArrayList~Sudoku~, Position) void
        - isInsideGrid(Position) boolean
        + countPossiblePlays(Position) int
        ~ getColor(int) String
        + getPossiblePlays(Position) Set~String~
        + addRule(Rule) void
        - containRule(Rule) boolean
        - removeCell(Position) void
        + hashCode() int
        + symbolLeftRule(Rule) Set~String~
        + initCells() void
        + print() void
        boolean complete
        ArrayList~Set~String~~ symbols
        Position size
    }
    class HistoryMove {
        + HistoryMove(String, Position)
        + HistoryMove()
        + HistoryMove(Set~String~, Position)
        + completed() void
        + equals(Set~String~) boolean
        + add(String) void
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
        + hashCode() int
        + toString() String
        + addX(int) Position
        + equals(Object) boolean
        + add(Position) Position
        + max(Position) Position
        + addXi(int) void
        + addYi(int) void
        + addY(int) Position
        + addi(Position) void
        + add(int) Position
        + min(Position) Position
        + negative() Position
        int x
        int y
    }
    class PreviousState {
        + PreviousState(Position, String, ArrayList~Set~String~~)
        + toString() String
    }
    class RowRule {
        + RowRule(Position, Position)
        + RowRule()
        + RowRule(Position, int)
    }
    class Rule {
        ~ Rule(Position)
        ~ Rule()
        ~ Rule(Set~Position~)
        - Set~Position~ rulePositions
        - int indexSymbols
        + add(Position) void
        + offsetRepositioning(Position) void
        + toString() String
        int indexSymbols
        Set~Position~ rulePositions
    }
    class Solver {
        # Solver(Grid)
        # chooseRandomValue(Set~String~) String
        + solve() void
        # getHistoryMoveFromPosition(Position) HistoryMove
        # rollBack(PreviousState) void
        ArrayList~Position~ positionsCompleted
    }
    class Sudoku {
        + Sudoku(int, Set~String~, Position)
        + Sudoku(int, Set~String~, int)
        + Sudoku(int, Set~String~)
        + Sudoku(int, Set~String~, ArrayList~Rule~, int)
        + Sudoku(int, Set~String~, ArrayList~Rule~)
        + Sudoku(int, Set~String~, ArrayList~Rule~, Position)
        - int size
        - Position offsetPosition
        - ArrayList~Rule~ rules
        - Set~String~ symbols
        + remove(Rule) void
        + add(Rule) void
        + addColumnRules() void
        - isInsideOfSudoku(Position) boolean
        + getRule(int) Rule
        + addRowRules() void
        Position minPosition
        Set~String~ symbols
        Position offsetPosition
        int size
        Position maxPosition
        ArrayList~Rule~ rules
        int numberRule
    }
    class SudokuClassic {
        + SudokuClassic(int)
        + SudokuClassic(int, int)
        + SudokuClassic(int, Position)
        - generateSymbols(int) Set~String~
    }
    class WaveFunctionCollapse {
        + WaveFunctionCollapse(Grid)
        - chooseRandomCell(ArrayList~Position~) Position
        + solve() void
        Grid grid
        CellsEntropy positionsMinimumEntropy
    }

    BlockRule  -->  Rule
    Grid  -->  Builder
    ColumnRule  -->  Rule
    RowRule  -->  Rule
    SudokuClassic  -->  Sudoku
    WaveFunctionCollapse  -->  Solver
```