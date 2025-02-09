package sudoku.terminal;

/**
 * The MainTerminal class serves as the entry point for the terminal-based
 * Sudoku application.
 */
public class MainTerminal {

    /**
     * The main method that starts the terminal interface for playing and solving
     * Sudoku puzzles.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        PlayTerminal terminal = new PlayTerminal();
        terminal.start();
    }
}