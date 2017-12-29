package ru.spbau.tictactoe.Bot;

import android.support.annotation.NonNull;

/**
 * Class used for debug output.
 */
public class Logger {
    boolean debug;

    /**
     * Prints message to console.
     *
     * @param message is message to be printed
     */
    public void printLog(@NonNull String message) {
        if (debug) {
            System.out.println(message);
        }
    }

    /**
     * Prints message to console in c++ format.
     * @param formatString is formatted string
     * @param args are args to be printed
     */
    public void printfLog(@NonNull String formatString, Object... args) {
        if (debug) {
            System.out.printf(formatString, args);
        }
    }
}
