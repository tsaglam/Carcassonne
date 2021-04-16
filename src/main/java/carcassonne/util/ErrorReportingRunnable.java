package carcassonne.util;

import carcassonne.view.util.GameMessage;

/**
 * Runnable that does not swallow any {@link Throwable} when executed in an executor service. Errors and exceptions are
 * reported as an game message.
 * @see GameMessage
 * @author Timur Saglam
 */
public class ErrorReportingRunnable implements Runnable {

    private static final String EMPTY_MESSAGE = "";
    private final Runnable wrappedTask;
    private final String messagePrefix;

    /**
     * Creates an error-reporting task by wrapping any runnable.
     * @param wrappedTask is the wrapped runnable.
     */
    public ErrorReportingRunnable(Runnable wrappedTask) {
        this(wrappedTask, EMPTY_MESSAGE);
    }

    /**
     * Creates an error-reporting task by wrapping any runnable.
     * @param wrappedTask is the wrapped runnable.
     * @param messagePrefix is a message prefix for the game message.
     */
    public ErrorReportingRunnable(Runnable wrappedTask, String messagePrefix) {
        this.wrappedTask = wrappedTask;
        this.messagePrefix = messagePrefix;
    }

    @Override
    public void run() {
        try {
            wrappedTask.run();
        } catch (Error | RuntimeException exception) {
            exception.printStackTrace();
            reportError(exception);
        } catch (Exception exception) {
            reportError(exception);
        }
    }

    private void reportError(Throwable throwable) {
        throwable.printStackTrace();
        GameMessage.showError(messagePrefix + throwable.getMessage());
    }
}