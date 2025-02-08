package sudoku;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import sudoku.rule.Rule;

public class SudokuSaver {
    public static void save(Grid grid, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Size: " + grid.getSize().getX() + "x" + grid.getSize().getY() + "\n");
            writer.newLine();
            ArrayList<Set<String>> symboles = grid.getSymboles();
            for (int i = 0; i < symboles.size(); i++) {
                writer.write("Symboles : " + symboles.get(i).toString() + "\n");
            }
            writer.newLine();
            writer.write("Rules:\n");

            for (Rule rule : grid.getRules()) {
                Set<Position> rulePositions = rule.getRulePositions();
                String indexSymbols = String.valueOf(rule.getIndexSymbols());
                String ruleType = rule.getClass().getSimpleName();
                writer.write(String.format("Rule %s (%s): %s\n", indexSymbols, ruleType, rulePositions.toString()));
            }
            writer.newLine();
            writer.write("Rules Cell:\n");
            for (int y = 0; y < grid.getSize().getY(); y++) {
                for (int x = 0; x < grid.getSize().getX(); x++) {
                    Cell cell = grid.getCell(new Position(x, y));
                    if (cell == null) {
                        continue;
                    }
                    ArrayList<Integer> rules = cell.getIdRules();
                    String rulesString = rules != null ? rules.toString() : "No rules";
                    writer.write(String.format("Cell (%d, %d): %s\n", x, y, rulesString));
                }
            }
            writer.newLine();
            writer.write("Grid:\n");

            for (int y = 0; y < grid.getSize().getY(); y++) {
                for (int x = 0; x < grid.getSize().getX(); x++) {
                    Position position = new Position(x, y);
                    String symbol = grid.getSymbol(position);
                    writer.write(symbol != null ? symbol : "-");
                    writer.write(" ");
                }
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}