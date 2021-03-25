package me.badstagram.vortex.exceptions;

public class CommandExecutionException extends Exception {
    /**
     * Thrown when a command fails to be executed.
     * @param cause The cause of the exception
     */
    public CommandExecutionException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
