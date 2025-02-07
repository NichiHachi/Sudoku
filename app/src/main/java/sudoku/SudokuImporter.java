package sudoku;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import sudoku.rule.Rule;

public class SudokuImporter {

    public static Grid importFromFile(String filename) {
        Grid grid = null;
        ArrayList<Rule> rules = new ArrayList<>();
        ArrayList<Set<String>> symbols = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean readingRules = false;
            boolean readingRulesCell = false;
            boolean readingGrid = false;
            int y = 0;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Size:")) {
                    String[] sizeParts = line.replace("Size:", "").replace("x", " ").trim().split(" ");
                    int gridSize = Integer.parseInt(sizeParts[0]);
                    grid = new Grid(gridSize, gridSize);
                    continue;
                } else if (line.startsWith("Symboles :")) {
                    String[] symbolParts = line.replace("Symboles :", "").replaceAll("[\\[\\]]", "").trim().split(",");
                    Set<String> symbolSet = new HashSet<>();
                    for (String symbol : symbolParts) {
                        symbolSet.add(symbol.trim());
                    }
                    symbols.add(symbolSet);
                    grid.setSymboles(symbols);
                    System.out.println("Symbols: " + symbols);
                    continue;
                } else if (line.startsWith("Rules:")) {
                    readingRules = true;
                    readingRulesCell = false;
                    readingGrid = false;
                    continue;
                } else if (line.startsWith("Rules Cell:")) {
                    readingRules = false;
                    readingRulesCell = true;
                    readingGrid = false;
                    continue;
                } else if (line.startsWith("Grid:")) {
                    readingRules = false;
                    readingRulesCell = false;
                    readingGrid = true;
                    continue;
                }

                if (readingRules) {
                    if (line.startsWith("Rule")) {
                        do {
                            String[] ruleParts = line.split(":");
                            String ruleId = ruleParts[0].substring(4, ruleParts[0].indexOf("(")).trim();
                            String ruleClassName = ruleParts[0]
                                    .substring(ruleParts[0].indexOf("(") + 1, ruleParts[0].indexOf(")")).trim();
                            Set<Position> positions = new HashSet<>();
                            String[] positionParts = ruleParts[1].replaceAll("[\\[\\]()]", "").trim().split(",");
                            for (int i = 0; i < positionParts.length; i += 2) {
                                int x = Integer.parseInt(positionParts[i].trim());
                                int z = Integer.parseInt(positionParts[i + 1].trim());
                                positions.add(new Position(x, z));
                            }
                            try {
                                Class<?> ruleClass = Class.forName("sudoku.rule." + ruleClassName);
                                Rule rule = (Rule) ruleClass.getConstructor(Set.class).newInstance(positions);
                                rule.setIndexSymbols(Integer.parseInt(ruleId));
                                rules.add(rule);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } while ((line = reader.readLine()) != null && !line.isEmpty());
                        grid.setRules(rules);
                        grid.initColors();
                    }
                } else if (readingRulesCell) {
                    if (line.startsWith("Cell")) {
                        do {
                            String[] parts = line.split(":");
                            String[] cellCoords = parts[0].replace("Cell", "").replaceAll("[()]", "").trim().split(",");
                            int cellX = Integer.parseInt(cellCoords[0].trim());
                            int cellY = Integer.parseInt(cellCoords[1].trim());

                            String[] values = parts[1].replaceAll("[\\[\\]]", "").trim().split(",");
                            ArrayList<Integer> cellValues = new ArrayList<>();
                            for (String value : values) {
                                cellValues.add(Integer.valueOf(value.trim()));
                            }

                            Cell cell = new Cell(cellValues);
                            grid.setCell(new Position(cellX, cellY), cell);
                        } while ((line = reader.readLine()) != null && !line.isEmpty());
                    }
                } else if (readingGrid) {
                    do {

                        // Process grid
                        String[] symboles = line.split(" ");
                        for (int x = 0; x < symboles.length; x++) {
                            String symbol = symboles[x];

                            if (!symbol.equals("-")) {
                                grid.insertSymbol(symbol, new Position(x, y));
                            }
                        }
                        y++;
                    } while ((line = reader.readLine()) != null && !line.isEmpty());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return grid;
    }
}