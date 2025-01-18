# Sudoku

Bulid the project :
```bash
./gradlew build
```

Run the project :
```bash
./bash run
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
    class Grid {
        - Cell[][] cells
        - Rule[] rules
        + Grid(Sudoku[] sudokus)void
        + print()void
    }
    
    class Sudoku {
        - Rule[] rules
        - Position[] rulesPosition
        - Position absolutePosition
        - Position size
        + initWithNormalRule() void
    }
    
    class Cell {
        - String value
        - int[] idRules
    }
    
    class Rule {
        - HashMap String -> String[] rules
    }
    
    class Position {
        - int x
        - int y
    }

    Grid o-- "1..*" Cell
    Grid o-- "1..*" Sudoku
    Grid ..> Rule
    Sudoku ..> Rule
    Sudoku ..> Position
    Grid ..> Position
```
