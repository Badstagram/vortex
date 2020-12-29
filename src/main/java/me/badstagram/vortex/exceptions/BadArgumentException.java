package me.badstagram.vortex.exceptions;


public class BadArgumentException extends Exception {


    private final String argument;
    private final boolean isMissing;


    /**
     * Thrown when a command argument is invalid or missing.
     *
     * @param argument
     *         The argument that's invalid/missing
     * @param isMissing
     *         if the argument is missing or invalid ({@code true} if missing. {@code false} if invalid)
     */
    public BadArgumentException(String argument, boolean isMissing) {
        super();
        this.argument = argument;
        this.isMissing = isMissing;
    }

    /**
     * Thrown when a command argument is invalid or missing
     *
     * @param argument
     *         The argument that's missing
     */
    public BadArgumentException(String argument) {
        super();
        this.argument = argument;
        this.isMissing = true;
    }

    public boolean isMissing() {
        return isMissing;
    }

    public String getArgument() {
        return argument;
    }
}
