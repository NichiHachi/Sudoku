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

/**
 * This class provides a terminal-based interface for playing and solving Sudoku
 * puzzles.
 */
public class PlayTerminal {

    private Grid grid;
    private Solver solver;

    /**
     * Constructs a PlayTerminal instance with no initial grid or solver.
     */
    public PlayTerminal() {
        this.grid = null;
        this.solver = null;
    }

    /**
     * Configures the logging level based on the provided command-line arguments.
     *
     * @param args the command-line arguments
     */
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

    /**
     * Starts the terminal interface for playing and solving Sudoku puzzles.
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Enter a grid to solve");
            System.out.println("2. Generate grids");
            System.out.println("3. Quit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> enterGrid(scanner);
                case 2 -> generateGrids(scanner);
                case 3 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Prompts the user to enter a grid file path and optionally solve the grid.
     *
     * @param scanner the Scanner object for user input
     */
    private void enterGrid(Scanner scanner) {
        System.out.print("Enter the grid file path: ");
        String filePath = scanner.nextLine();
        this.grid = SudokuImporter.importFromFile(filePath);
        System.out.println("Grid imported successfully.");
        System.out.println("Do you want to solve the grid? (Y/N)");
        String solve = scanner.nextLine();
        if (solve.equalsIgnoreCase("Y")) {
            double difficulty = -1;
            while (difficulty < 0.0 || difficulty > 1.0) {
                System.out.print("Enter the difficulty level (0.0 to 1.0): ");
                if (scanner.hasNextDouble()) {
                    difficulty = scanner.nextDouble();
                    scanner.nextLine();
                    if (difficulty < 0.0 || difficulty > 1.0) {
                        System.out.println("Please enter a value between 0.0 and 1.0.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a decimal number.");
                    scanner.next();
                }
            }
            GenerateSudoku generator = new GenerateSudoku(this.grid, difficulty);
            generator.generateSudoku(GenerateSudoku.SolverType.BACKTRACK_OPTIMIZED);
            this.grid = generator.getGrid();
            this.grid.print();
            System.out.println("Grid generated successfully.");
            gridResolution(scanner);
        }
    }

    /**
     * Prompts the user to generate a new Sudoku grid with a specified difficulty
     * level.
     *
     * @param scanner the Scanner object for user input
     */
    private void generateGrids(Scanner scanner) {
        double difficulty = -1;
        while (difficulty < 0.0 || difficulty > 1.0) {
            System.out.print("Enter the difficulty level (0.0 to 1.0): ");
            if (scanner.hasNextDouble()) {
                difficulty = scanner.nextDouble();
                scanner.nextLine();
                if (difficulty < 0.0 || difficulty > 1.0) {
                    System.out.println("Please enter a value between 0.0 and 1.0.");
                }
            } else {
                System.out.println("Invalid input. Please enter a decimal number.");
                scanner.next();
            }
        }

        System.out.print("Choose the grid type (1: simple, 2: multi sudoku): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.print("Enter the grid size: ");
                int size = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Does the grid have random blocks? (Y/N)");
                String randomBlock = scanner.nextLine();
                boolean random = randomBlock.equalsIgnoreCase("Y");
                this.grid = new Grid.Builder().addSudoku(new SudokuClassic(size, new Position(0), random)).build();
            }
            case 2 -> {
                System.out.print("Enter the number of grids: ");
                int size = scanner.nextInt();
                scanner.nextLine();
                Grid.Builder builder = new Grid.Builder();
                for (int i = 0; i < size; i++) {
                    System.out.println("Does the grid have random blocks? (Y/N)");
                    String randomBlock = scanner.nextLine();
                    boolean random = randomBlock.equalsIgnoreCase("Y");
                    System.out.println("Enter the size of grid " + (i + 1) + ": ");
                    int gridSize = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter the X position of grid " + (i + 1) + ": ");
                    int offsetX = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter the Y position of grid " + (i + 1) + ": ");
                    int offsetY = scanner.nextInt();
                    scanner.nextLine();
                    builder.addSudoku(new SudokuClassic(gridSize, new Position(offsetX, offsetY), random));
                }
                this.grid = builder.build();
            }
            default -> {
                System.out.println("Invalid option. Please try again.");
                return;
            }
        }

        if (this.grid == null) {
            System.out.println("Error generating the grid.");
            return;
        }

        GenerateSudoku generator = new GenerateSudoku(this.grid, difficulty);
        generator.generateSudoku(GenerateSudoku.SolverType.BACKTRACK_OPTIMIZED);
        this.grid = generator.getGrid();
        System.out.println("Grid generated successfully.");
        this.grid.print();
        System.out.println("Do you want to solve the grid? (Y/N)");
        String solve = scanner.nextLine();
        if (solve.equalsIgnoreCase("Y")) {
            gridResolution(scanner);
        }
    }

    /**
     * Prompts the user to solve the current grid either manually or using a solver.
     *
     * @param scanner the Scanner object for user input
     */
    public void gridResolution(Scanner scanner) {
        if (this.grid == null) {
            System.out.println("No grid to solve.");
            return;
        }
        System.out.println("Do you want to use a solver or solve it yourself? (S/M)");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("M")) {
            this.grid.playTerminal();
        }
        System.out.println("Which solver do you want to use? (1: WFC, 2: Backtracking)");
        int solverChoice = scanner.nextInt();
        scanner.nextLine();
        switch (solverChoice) {
            case 1 -> this.solver = new WaveFunctionCollapse(this.grid);
            case 2 -> this.solver = new BacktrackOptimized(this.grid);
            default -> {
                System.out.println("Invalid option. Please try again.");
                return;
            }
        }
        this.solver.solve();
        System.out.println("Grid solved successfully.");
        this.solver.getGrid().print();
    }
}