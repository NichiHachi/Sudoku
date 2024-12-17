package sudoku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Grid {
    private Cell[][] cells;
    private final int size;
    private int idCounter = 0;
    Constraint[][] blockConstraints;

    public Grid(int size) {
        this.size = size;
        this.cells = new Cell[size][size];

        List<Integer> factors = primeFactors(size);
        int blockHeight = factors.get(0);
        int blockWidth = factors.get(1);
        blockConstraints = new Constraint[blockHeight][blockWidth];

        for (int blockRow = 0; blockRow < blockHeight; blockRow++) {
            for (int blockCol = 0; blockCol < blockWidth; blockCol++) {
                HashMap<String, String[]> rules = new HashMap<>();
                blockConstraints[blockRow][blockCol] = new Constraint(new Rule(rules), idCounter);
                idCounter++;
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int blockRow = i / blockWidth;
                int blockCol = j / blockHeight;
                Constraint constraint = blockConstraints[blockRow][blockCol];
                cells[i][j] = new Cell(new Position(i, j), new Element("_"), constraint.getId());
            }
        }
    }

    public static List<Integer> primeFactors(int number) {
        List<Integer> factors = new ArrayList<>();
        for (int i = 2; i <= number; i++) {
            while (number % i == 0) {
                factors.add(i);
                number /= i;
            }
        }
        return factors;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }

    public void show() {
        String[] colors = {
                "\u001B[31m", // Rouge
                "\u001B[32m", // Vert
                "\u001B[33m", // Jaune
                "\u001B[34m", // Bleu
                "\u001B[35m", // Magenta
                "\u001B[36m", // Cyan
                "\u001B[37m", // Blanc
                "\u001B[90m", // Gris
                "\u001B[91m", // Rouge clair
                "\u001B[92m", // Vert clair
                "\u001B[93m", // Jaune clair
                "\u001B[94m", // Bleu clair
                "\u001B[95m", // Magenta clair
                "\u001B[96m", // Cyan clair
                "\u001B[97m" // Blanc clair
        };
        String resetColor = "\u001B[0m";

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int constraintId = cells[i][j].getIdConstraints();
                String color = colors[constraintId % colors.length];
                System.out.print(color + cells[i][j].getElement().getValue() + " " + resetColor);
            }
            System.out.println();
        }
    }

}
