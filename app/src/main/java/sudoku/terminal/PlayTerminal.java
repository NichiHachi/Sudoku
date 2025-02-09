package sudoku.terminal;

import java.util.Scanner;

import solvers.Solver;
import solvers.backtrack.BacktrackOptimized;
import solvers.wfc.WaveFunctionCollapse;
import sudoku.GenerateSudoku;
import sudoku.Grid;
import sudoku.Position;
import sudoku.configuration.SudokuImporter;
import sudoku.sudoku.SudokuClassic;

public class PlayTerminal {

    private Grid grid;
    private Solver solver;

    public PlayTerminal() {
        this.grid = null;
        this.solver = null;
    }

    public static void configureLogging(String[] args) {
        String logLevel = "WARN";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--debug") || args[i].equals("-d")) {
                logLevel = "DEBUG";
            } else if (args[i].equals("--info") || args[i].equals("-i")) {
                logLevel = "INFO";
            } else if (args[i].equals("--trace") || args[i].equals("-t")) {
                logLevel = "TRACE";
            }
        }
        System.setProperty("LOG_LEVEL", logLevel);
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Entrer une grille à résoudre");
            System.out.println("2. Générer des grilles");
            System.out.println("3. Quitter");
            System.out.print("Choisissez une option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> enterGrid(scanner);
                case 2 -> generateGrids(scanner);
                case 3 -> {
                    System.out.println("Au revoir!");
                    return;
                }
                default -> System.out.println(
                    "Option invalide. Veuillez réessayer."
                );
            }
        }
    }

    private void enterGrid(Scanner scanner) {
        System.out.print("Entrez le chemin du fichier de la grille: ");
        String filePath = scanner.nextLine();
        this.grid = SudokuImporter.importFromFile(filePath);
        System.out.println("Grille importée avec succès.");
        System.out.println("Voulez-vous résoudre la grille ? (O/N)");
        String solve = scanner.nextLine();
        if (solve.equalsIgnoreCase("O")) {
            double difficulty = -1;
            while (difficulty < 0.0 || difficulty > 1.0) {
                System.out.print(
                    "Entrez le niveau de difficulté (0.0 à 1.0): "
                );
                if (scanner.hasNextDouble()) {
                    difficulty = scanner.nextDouble();
                    scanner.nextLine();
                    if (difficulty < 0.0 || difficulty > 1.0) {
                        System.out.println("Veuillez entrer une valeur entre 0,0 et 1,0.");
                    }
                } else {
                    System.out.println(
                        "Entrée invalide. Veuillez entrer un nombre décimal."
                    );
                    scanner.next();
                }
            }
            GenerateSudoku generator = new GenerateSudoku(
                this.grid,
                difficulty
            );
            generator.generateSudoku(
                GenerateSudoku.SolverType.BACKTRACK_OPTIMIZED
            );
            this.grid = generator.getGrid();
            this.grid.print();
            System.out.println("Grille générée avec succès.");
            gridResolution(scanner);
        }
    }

    private void generateGrids(Scanner scanner) {
        double difficulty = -1;
        while (difficulty < 0.0 || difficulty > 1.0) {
            System.out.print("Entrez le niveau de difficulté (0,0 à 1,0): ");
            if (scanner.hasNextDouble()) {
                difficulty = scanner.nextDouble();
                scanner.nextLine();
                if (difficulty < 0.0 || difficulty > 1.0) {
                    System.out.println("Veuillez entrer une valeur entre 0,0 et 1,0.");
                }
            } else {
                System.out.println(
                    "Entrée invalide. Veuillez entrer un nombre décimal."
                );
                scanner.next();
            }
        }

        System.out.print("Choix de la grille (1: simple, 2: multi sudoku): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.print("Entrez la taille de la grille : ");
                int size = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Est ce que la grille à des blocs aléatoires ? (O/N)");
                String randomBlock = scanner.nextLine();
                boolean random = randomBlock.equalsIgnoreCase("O");
                this.grid = new Grid.Builder().addSudoku(new SudokuClassic(size, new Position(0), random)).build();
            }
            case 2 -> {
                System.out.print("Entrez le nombre de grilles : ");
                int size = scanner.nextInt();
                scanner.nextLine();
                Grid.Builder builder = new Grid.Builder();
                for (int i = 0; i < size; i++) {
                    System.out.println("Est ce que la grille à des blocs aléatoires ? (O/N)");
                    String randomBlock = scanner.nextLine();
                    boolean random = randomBlock.equalsIgnoreCase("O");
                    System.out.println("Entrer la taille de la grille " + (i + 1) + " : ");
                    int gridSize = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println(
                        "Entrer la position X de la grille " + (i + 1) + " : "
                    );
                    int offsetX = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println(
                        "Entrer la position Y de la grille " + (i + 1) + " : "
                    );
                    int offsetY = scanner.nextInt();
                    scanner.nextLine();
                    builder.addSudoku(new SudokuClassic(gridSize, new Position(offsetX, offsetY), random));
                }
                this.grid = builder.build();
            }
            default -> {
                System.out.println("Option invalide. Veuillez réessayer.");
                return;
            }
        }

        if (this.grid == null) {
            System.out.println("Erreur lors de la génération de la grille.");
            return;
        }

        GenerateSudoku generator = new GenerateSudoku(this.grid, difficulty);
        generator.generateSudoku(GenerateSudoku.SolverType.BACKTRACK_OPTIMIZED);
        this.grid = generator.getGrid();
        System.out.println("Grille générée avec succès.");
        this.grid.print();
        System.out.println("Voulez-vous résoudre la grille ? (O/N)");
        String solve = scanner.nextLine();
        if (solve.equalsIgnoreCase("O")) {
            gridResolution(scanner);
        }
    }

    public void gridResolution(Scanner scanner) {
        if (this.grid == null) {
            System.out.println("Aucune grille à résoudre.");
            return;
        }
        System.out.println(
            "Voulez vous utilisez un solver ou la résoudre vous même ? (S/M)"
        );
        String choice = scanner.nextLine();
        if (choice.equals("M")) {
            this.grid.playTerminal();
        }
        System.out.println(
            "Quel solver voulez vous utiliser ? (1: WFC) (2: Backtracking)"
        );
        int solverChoice = scanner.nextInt();
        scanner.nextLine();
        switch (solverChoice) {
            case 1 -> this.solver = new WaveFunctionCollapse(this.grid);
            case 2 -> {
                this.solver = new BacktrackOptimized(this.grid);
            }
            default -> {
                System.out.println("Option invalide. Veuillez réessayer.");
                return;
            }
        }
        this.solver.solve();
        System.out.println("Grille résolue avec succès.");
        this.solver.getGrid().print();
    }
}
