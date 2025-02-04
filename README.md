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