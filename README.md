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
    class Grid {
        - Cell[][] cells
        - Rule[] rules
        - Position size
        + Grid(Sudoku[] sudokus)void
        + print()void
    }

    class Sudoku {
        - Rule[] rules
        - Position[] rulesPosition
        - Position offsetPosition
        - Position size
        + Sudoku(String[] values, Position offset)
        + Sudoku(String[] values)
    }

    class Cell {
        - String value
        - int[] idRules
    }

    class Rule {
        - HashMap String -> Set[String] rules
        + Rule(String[])
        + Rule(HashMap String -> Set[String])
        + getPossibleMove() Set[String]
        + add(String key, Set[String] value) void
        + mergingRule(Rule rule) void
        + isValid(String value) boolean
        + placeValue(String value): boolean
    }

    class Position {
        - int x
        - int y
    }

    Grid o-- "1..*" Cell
    Grid --> "1..*" Sudoku
    Grid ..> Rule
    Sudoku ..> Rule
    Sudoku ..> Position
    Grid ..> Position
```
