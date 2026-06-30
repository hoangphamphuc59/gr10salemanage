package ui;

/**
 * Utility class for ANSI colored console output.
 * Provides green for success messages and red for error messages.
 */
public class ConsoleColor {

    // ANSI escape codes
    public static final String RESET  = "\033[0m";
    public static final String RED    = "\033[31m";
    public static final String GREEN  = "\033[32m";

    /**
     * Returns the message wrapped in green ANSI color codes.
     */
    public static String success(String msg) {
        return GREEN + msg + RESET;
    }

    /**
     * Returns the message wrapped in red ANSI color codes.
     */
    public static String error(String msg) {
        return RED + msg + RESET;
    }

    /**
     * Prints a success message in green to stdout.
     */
    public static void printSuccess(String msg) {
        System.out.println(GREEN + msg + RESET);
    }

    /**
     * Prints an error message in red to stdout.
     */
    public static void printError(String msg) {
        System.out.println(RED + msg + RESET);
    }
}
