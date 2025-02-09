package utils;

public class Colors {

    // Regular foreground colors
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    // Text styles
    public static final String BOLD = "\u001B[1m";
    public static final String DIM = "\u001B[2m";
    public static final String ITALIC = "\u001B[3m";
    public static final String UNDERLINE = "\u001B[4m";
    public static final String BLINK = "\u001B[5m";
    public static final String REVERSE = "\u001B[7m";
    public static final String HIDDEN = "\u001B[8m";
    public static final String STRIKE = "\u001B[9m";

    // Reset
    public static final String RESET = "\u001B[0m";

    // Color meanings
    public static final String DEBUG_COLOR = Colors.BLUE;
    public static final String SUCCESS_COLOR = Colors.GREEN;
    public static final String WARNING_COLOR = Colors.YELLOW;
    public static final String ERROR_COLOR = Colors.RED;
    public static final String INFO_COLOR = Colors.CYAN;
    public static final String HIGHLIGHT_COLOR = Colors.MAGENTA;
}
